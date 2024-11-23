package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.project.StoryImageResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.StoryImage;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.StoryImageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final FundingRepository fundingRepository;
    private final StoryImageRepository storyImageRepository;
    private final FundingFileService fileService;

    @Transactional
    public ResponseEntity<? super StoryImageResponseDto> file_upload(Long funding_id, MultipartFile image) {
        if (image.isEmpty()) {
            System.out.println("파일이 존재하지 않음");
            return StoryImageResponseDto.not_existed_file();
        }
        String uuid_name = fileService.file_upload("story_image", image);
        if (uuid_name == null) {
            System.out.println("파일 저장 실패");
            return ResponseDto.databaseError();
        }
        try{
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {  //존재하지 않는 펀딩
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            StoryImage storyImage = new StoryImage(uuid_name, funding);
            storyImageRepository.save(storyImage);

        } catch(Exception e){
            throw new RuntimeException("이미지를 읽는 중 오류 발생", e);
        }
        System.out.println("파일이름은 "+uuid_name);
        return StoryImageResponseDto.success(uuid_name);
    }

    // 게시글 저장
    @Transactional
    public ResponseEntity<ResponseDto> savePost(Long funding_id, String content) {

        try{
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {  //존재하지 않는 펀딩
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();
            funding.setStory(content);

            return ResponseDto.success();
        } catch(Exception e){
            return ResponseDto.databaseError();
        }
    }

}
