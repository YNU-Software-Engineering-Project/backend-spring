package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.wirtefunding.FundingStoryRequestDto;
import sg.backend.dto.response.writefunding.project.GetProjectResponseDto;
import sg.backend.dto.response.writefunding.file.DeleteFileResponseDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.file.UploadImageResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.IntroImage;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.IntroImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingStoryService {

    @Value("${file.path}")
    private String filePath;

    @Autowired
    FundingFileService fileService;

    @Autowired
    private FundingRepository fundingRepository;
    @Autowired
    private IntroImageRepository imageRepository;
    @Autowired
    private IntroImageRepository introImageRepository;

    @Transactional
    public ResponseEntity<? super GetProjectResponseDto> getProject(Long funding_id) {
        String title;
        String main_url;
        String main_uuid;
        String[] images_url;
        String[] images_uuid;
        String summary;
        try{
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()){
                return GetProjectResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            title = funding.getTitle();

            String main = funding.getMainImage();
            if(main == null){
                main_url = null;
                main_uuid = null;
            } else{
                main_uuid = main.replace("/app/data/funding_image/", "");
                main_url = "http://localhost:8080/file/view/funding_image/" + main_uuid;
            }

            List<IntroImage> images = imageRepository.findAllByFunding(funding);
            if(images.isEmpty()){
                images_url = null;
                images_uuid = null;
            } else{
                ArrayList<String> urls = new ArrayList<>();
                ArrayList<String> uuids = new ArrayList<>();
                for (IntroImage img : images) {
                    urls.add("http://localhost:8080/file/view/funding_image/" + img.getUuid());
                    uuids.add(img.getUuid());
                }
                images_url = urls.toArray(new String[urls.size()]);
                images_uuid = uuids.toArray(new String[0]);
            }

            summary = funding.getProjectSummary();

        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetProjectResponseDto.success(title, main_url, main_uuid,images_url, images_uuid,summary);
    }

    @Transactional
    public ResponseEntity<? super ModifyContentResponseDto> modify_project(Long funding_id, FundingStoryRequestDto dto){
        try{
            String title = dto.getTitle();
            String summary = dto.getSummary();

            Optional<Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()){
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            funding.setTitle(title);
            funding.setProjectSummary(summary);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyContentResponseDto.success();
    }

    @Transactional//이미지 업로드
    public ResponseEntity<? super UploadImageResponseDto> upload_images(Long funding_id, MultipartFile file, Boolean store){

        String uuid_name;
        String url;

        //not_existed_File 추가
        if(file.isEmpty()){
            return UploadImageResponseDto.not_existed_file();
        }

        String[] allowedTypes = {"image/jpeg", "image/png", "image/gif"};
        // 파일 형식 검증
        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String type : allowedTypes) {
            if (type.equals(contentType)) {
                isValidType = true; // 허용되는 형식을 찾으면 true로 설정
                break;              // 일치하는 형식을 찾았으므로 루프 종료
            }
        }
        if (!isValidType) { return UploadImageResponseDto.unsupported_file(); }

        try{
            uuid_name = fileService.file_upload("funding_image", file);
            String savePath = filePath + "funding_image/" + uuid_name;
            if(uuid_name == null){
                return ResponseDto.databaseError();
            }
            url = "http://localhost:8080/file/view/funding_image/" + uuid_name;

            Optional<Funding> optionalFunding = fundingRepository.findById(funding_id);
            if (optionalFunding.isEmpty()) {
                return UploadImageResponseDto.not_existed_post();
            }
            Funding funding = optionalFunding.get();   //id에 대한 펀딩객체 가져오기

            if(store){
                IntroImage introImage = new IntroImage(funding,uuid_name, savePath);
                imageRepository.save(introImage);
            } else{
                funding.setMainImage(savePath);
            }

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return UploadImageResponseDto.success(uuid_name, url);
    }

    @Transactional
    public ResponseEntity<? super DeleteFileResponseDto> deleteMain(Long funding_id){

        try{
            Optional<Funding> options = fundingRepository.findById(funding_id);
            if (options.isEmpty()) {
                return DeleteFileResponseDto.not_existed_post();
            }
            Funding funding = options.get();

            String file_path = funding.getMainImage();
            if(file_path == null || file_path.isEmpty()){
                return DeleteFileResponseDto.not_existed_file();
            }
            if(!fileService.file_delete(file_path)){
                return ResponseDto.databaseError();
            }

            funding.setMainImage(null);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteFileResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super DeleteFileResponseDto> deleteImage(String uuid){

        try{
            IntroImage image = introImageRepository.findByUuid(uuid);

            if( image == null || image.getFpath() == null || image.getFpath().isEmpty()){
                return DeleteFileResponseDto.not_existed_file();
            }
            if(!fileService.file_delete(image.getFpath())){
                return ResponseDto.databaseError();
            }

            introImageRepository.delete(image);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteFileResponseDto.success();
    }
}
