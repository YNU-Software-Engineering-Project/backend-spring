package sg.backend.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.FundingInfoRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.file.DeleteFileResponseDto;
import sg.backend.dto.response.file.UploadInfoFileResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.DocumentRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.FundingTagRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class FundingInfoService {

    @Value("${file.path}")
    private String filePath;

    @Autowired
    FileService fileService;

    //private final UserRepository userRepository;

    @Autowired
    private FundingRepository fundingRepository;
    @Autowired
    private DocumentRepository documentRepository;
    private final FundingTagRepository fundingTagRepository;

    String large;
    String small;
    String organizer_name;
    String organizer_email;
    String tax_email;
    String start;
    String end;
    String amount;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    public ResponseEntity<? super GetInfoResponseDto> getInfo(Long funding_id){
        String[] tag;
        String id_card_url;
        String[] document_url;
        try{
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return GetInfoResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            large = funding.getCategory().getMessage();
            small = funding.getSubCategory().getMessage();

            List<FundingTag> fundingTags = fundingTagRepository.findAllByFunding(funding);
            ArrayList<String> tags = new ArrayList<>();
            for(FundingTag tag_name: fundingTags){
                tags.add(tag_name.getTag_name());
            }
            tag = tags.toArray(new String[0]);

            String id_card = funding.getOrganizerIdCard();
            id_card = id_card.replace("/app/data/project_document/", "");
            id_card_url = "http://localhost:8080/file/view/funding_image/"+ id_card;

            organizer_name = funding.getOrganizerName();
            organizer_email = funding.getOrganizerEmail();
            tax_email = funding.getTaxEmail();
            
            List<Document> documents = documentRepository.findAllByFunding(funding);
            ArrayList<String> urls = new ArrayList<>();
            for(Document docu: documents){
                urls.add("http://localhost:8080/file/view/project_document/"+ docu.getUuid());
            }
            document_url = urls.toArray(new String[0]);

            start = funding.getStartDate().format(format);
            start = start.replace(" 11:59", "");
            end = funding.getEndDate().format(format);
            end = end.replace(" 11:59", "");

            amount = funding.getTargetAmount().toString();

        } catch(Exception e){
            return ResponseDto.databaseError();
        }
        return GetInfoResponseDto.success(large,small,tag, id_card_url,organizer_name,organizer_email,tax_email,document_url,start,end,amount);
    }


    @Transactional  //펀딩 정보 수정
    public ResponseEntity<? super ModifyContentResponseDto> modifyInfo(Long funding_id, FundingInfoRequestDto dto) {
        try {
            large = dto.getLarge_category();
            Category large_category = Category.getCategory(large);
            small = dto.getSmall_category();
            SubCategory small_category = SubCategory.getCategory(small);

            //이메일 형식은 프론트에서 검사하고 잘못됐으면 오류 메세지 나가고 백엔드엔 null로 전달
            organizer_name = dto.getOrganizer_name();
            organizer_email = dto.getOrganizer_email();
            tax_email = dto.getTax_email();

            String start = dto.getStart_date();
            LocalDateTime start_date;
            if(start == null){
                start_date = null;
            } else{
                start = start.concat(" 11:59");
                start_date = LocalDateTime.parse(start,format);
            }
            String end = dto.getEnd_date();
            LocalDateTime end_date;
            if(end == null){
                end_date = null;
            } else{
                end = end.concat(" 11:59");
                end_date = LocalDateTime.parse(end, format);
            }

            Integer target_amount = parseInt(dto.getTarget_amount());

            Optional <Funding> option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            funding.setCategory(large_category);
            funding.setSubCategory(small_category);
            funding.setOrganizerName(organizer_name);  //객체 내용 변경
            funding.setOrganizerEmail(organizer_email);
            funding.setTaxEmail(tax_email);
            funding.setStartDate(start_date);
            funding.setEndDate(end_date);
            funding.setTargetAmount(target_amount);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ModifyContentResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super UploadInfoFileResponseDto> uploadFile(Long funding_id, MultipartFile file, Boolean store){

        String originalFilename;
        String uuid_name;
        String url;
        try{
            if(file.isEmpty()){  //not_existed_file
                return UploadInfoFileResponseDto.not_existed_file();
            }

            originalFilename = file.getOriginalFilename();
            uuid_name = fileService.file_upload("project_document", file);
            String file_path = filePath + "project_document/" + uuid_name;
            if(uuid_name == null){
                return ResponseDto.databaseError();
            }

            Optional<Funding> options = fundingRepository.findById(funding_id);
            if (options.isEmpty()) {
                return UploadInfoFileResponseDto.not_existed_post();
            }
            Funding funding = options.get();

            if(store){
                Document document = new Document(funding, originalFilename, uuid_name, file_path);
                documentRepository.save(document);
            } else{
                funding.setOrganizerIdCard(file_path);
            }
            url = "http://localhost:8080/file/view/project_document/"+ uuid_name;
        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return UploadInfoFileResponseDto.success(originalFilename, uuid_name, url);
    }

    @Transactional
    public ResponseEntity<? super DeleteFileResponseDto> deleteIDcard(Long funding_id){

        try{
            Optional<Funding> options = fundingRepository.findById(funding_id);
            if (options.isEmpty()) {
                return DeleteFileResponseDto.not_existed_post();
            }
            Funding funding = options.get();

            String file_path = funding.getOrganizerIdCard();
            if(!fileService.file_delete(file_path)){
                return DeleteFileResponseDto.not_existed_file();
            }

            funding.setOrganizerIdCard(null);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteFileResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super DeleteFileResponseDto> deleteDocument(String uuid){

        try{
            Document document = documentRepository.findByUuid(uuid);
            if(document.getFpath() == null){
                return DeleteFileResponseDto.not_existed_file();
            }

            if(!fileService.file_delete(document.getFpath())){
                return ResponseDto.databaseError();
            }

            documentRepository.delete(document);

        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteFileResponseDto.success();
    }

}
