package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.wirtefunding.FundingInfoRequestDto;
import sg.backend.dto.request.wirtefunding.InsertTagRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.writefunding.GetFundingMainResponseDto;
import sg.backend.dto.response.writefunding.RegisterResponseDto;
import sg.backend.dto.response.writefunding.file.DeleteFileResponseDto;
import sg.backend.dto.response.writefunding.file.UploadInfoFileResponseDto;
import sg.backend.dto.response.writefunding.DeleteDataResponseDto;
import sg.backend.dto.response.writefunding.project.GetInfoResponseDto;
import sg.backend.dto.response.writefunding.project.InsertTagResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.DocumentRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.TagRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingInfoService {

    private final FundingRepository fundingRepository;
    private final DocumentRepository documentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    private final FundingFileService fileService;
    @Value("${file.path}")
    private String filePath;

    String category;
    String organizer_name;
    String organizer_email;
    String tax_email;
    String start;
    String end;
    String amount;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ResponseEntity<? super RegisterResponseDto> register(Long user_id){
        try{
            Optional<User> option = userRepository.findById(user_id);
            if(option.isEmpty()) {
                return ResponseDto.noExistUser();
            }
            User user = option.get();

            String school_email = user.getSchoolEmail();
            if(school_email == null || school_email.isEmpty()){
                return RegisterResponseDto.not_schoolemail();
            }
        } catch(Exception e){
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }


    @Transactional   //펀딩 제일 처음 들어갔을때 나오는 이미지
    public ResponseEntity<? super GetFundingMainResponseDto> getFundingMain(Long funding_id){
        String url;
        String title;
        try{
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if (option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            String main = funding.getMainImage();
            if (main == null) {
                url = null;
            } else {
                main = main.replace("/app/data/funding_image/", "");
                url = "http://localhost:8080/file/view/funding_image/" + main;
            }

            title = funding.getTitle();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetFundingMainResponseDto.success(url, title);
    }

    @Transactional  //정보작성 페이지 내용 get
    public ResponseEntity<? super GetInfoResponseDto> getInfo(Long funding_id){
        String[] tag;
        Long[] tag_id;

        String id_card_url;
        String id_card_uuid;

        String[] document_url;
        String[] document_name;
        String[] document_uuid;
        try {
            Optional<Funding> option = fundingRepository.findById(funding_id);
            if (option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            category = funding.getCategory().getMessage(); // null이면 해당없음 메시지

            List<Tag> fundingTags = tagRepository.findAllByFunding(funding);
            if (fundingTags.isEmpty()) {  // 빈 리스트 체크
                tag = null;
                tag_id = null;
            } else {
                ArrayList<String> tags = new ArrayList<>();
                ArrayList<Long> tag_ids = new ArrayList<>();
                for (Tag tag_name : fundingTags) {
                    tags.add(tag_name.getTag_name());
                    tag_ids.add(tag_name.getTag_id());
                }
                tag = tags.toArray(new String[0]);
                tag_id = tag_ids.toArray(new Long[0]);
            }

            String id_card = funding.getOrganizerIdCard();
            if (id_card == null) {
                id_card_url = null;
                id_card_uuid = null;
            } else {
                id_card_uuid = id_card.replace("/app/data/project_document/", "");
                id_card_url = "http://localhost:8080/file/view/project_document/" + id_card;
            }

            organizer_name = funding.getOrganizerName();
            organizer_email = funding.getOrganizerEmail();
            tax_email = funding.getTaxEmail();

            List<Document> documents = documentRepository.findAllByFunding(funding);
            if (documents.isEmpty()) {  // 빈 리스트 체크
                document_url = new String[0];  // 빈 배열 반환
                document_name = new String[0];
                document_uuid = new String[0];
            } else {
                ArrayList<String> urls = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> uuids = new ArrayList<>();
                for (Document docu : documents) {
                    names.add(docu.getName());
                    uuids.add(docu.getUuid());
                    urls.add("http://localhost:8080/file/view/project_document/" + docu.getUuid());
                }
                document_url = urls.toArray(new String[0]);
                document_name = names.toArray(new String[0]);
                document_uuid = uuids.toArray(new String[0]);
            }

            LocalDateTime start_day = funding.getStartDate();
            if (start_day == null) {
                start = null;
            } else {
                start = start_day.format(format);
                start = start.replace(" 00:01:01", "");
            }
            LocalDateTime end_day = funding.getEndDate();
            if (end_day == null) {
                end = null;
            } else {
                end = end_day.format(format);
                end = end.replace(" 11:59:59", "");
            }

            Integer amou = funding.getTargetAmount();
            if(amou == null){
                amount = null;
            } else{
                amount = amou.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetInfoResponseDto.success(category, tag, tag_id, id_card_url, id_card_uuid, organizer_name,
                organizer_email, tax_email, document_url, document_name, document_uuid, start, end, amount);
    }



    @Transactional  //펀딩 정보 수정 + 펀딩 등록하기
    public ResponseEntity<? super RegisterResponseDto> modifyInfo(Long funding_id, FundingInfoRequestDto dto, Long user_id, Boolean type) {
        try {
            category = dto.getCategory(); //맞는 카테고리 전달되도록하기
            Category cate;
            if(category == null){
                cate = Category.NONE;
            } else{
                cate = Category.getCategory(category);
            }

            //이메일 형식 추가함 => 이메일 형식 안 맞으면 database error 뜸
            organizer_name = dto.getOrganizer_name();
            organizer_email = dto.getOrganizer_email();
            tax_email = dto.getTax_email();

            if(type){  //등록하기 연산
                if(organizer_name == null || organizer_email == null || tax_email == null){
                    return ResponseDto.noPermission();
                }
            }

            String start = dto.getStart_date(); // => 날짜 형식 안 맞으면 DBE
            LocalDateTime start_date;
            if(start == null){ //날짜 형식 맞는지 프론트가 구분
                start_date = null;
            } else{
                start = start.concat(" 00:01:01");
                start_date = LocalDateTime.parse(start,format);
            }
            String end = dto.getEnd_date();
            LocalDateTime end_date;
            if(end == null){ //날짜 형식 구분
                end_date = null;
            } else{
                end = end.concat(" 11:59:59");
                end_date = LocalDateTime.parse(end, format);
            }

            //=> 숫자형식 안 맞으면 DBE
            String amount = dto.getTarget_amount(); //숫자가 들어갔는지 프론트가 구분
            Integer target_amount;
            if(amount == null){
                target_amount = null;
            } else{
                target_amount = Integer.parseInt(amount);
            }

            if(type){ //새로운 펀딩 생성후 데이터 저장하고 펀딩을 데이터베이스에 저장
                Optional<User> option = userRepository.findById(user_id);
                if(option.isEmpty()) {
                    return ResponseDto.noExistUser();
                }
                User user = option.get();

                Funding new_funding = new Funding(organizer_name,organizer_email,tax_email, user);
                new_funding.setCategory(cate);
                new_funding.setStartDate(start_date);
                new_funding.setEndDate(end_date);
                new_funding.setTargetAmount(target_amount);

                fundingRepository.save(new_funding);
                Long new_funding_id = new_funding.getFunding_id();

                return RegisterResponseDto.success(new_funding_id);
            } else{
                Optional <Funding> option = fundingRepository.findById(funding_id);
                if(option.isEmpty()) {
                    return ResponseDto.noExistFunding();
                }
                Funding funding = option.get();

                funding.setCategory(cate);
                funding.setOrganizerName(organizer_name);  //객체 내용 변경
                funding.setOrganizerEmail(organizer_email);
                funding.setTaxEmail(tax_email);
                funding.setStartDate(start_date);
                funding.setEndDate(end_date);
                funding.setTargetAmount(target_amount);

                return ResponseDto.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }


    @Transactional
    public ResponseEntity<? super InsertTagResponseDto> insertTag(Long id, InsertTagRequestDto tag_name){
        Long tag_id;
        try{
            Optional<Funding> option = fundingRepository.findById(id);
            if(option.isEmpty()) {
                return InsertTagResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            if(tag_name.getTagName() == null){
                return InsertTagResponseDto.not_existed_data();
            } else{
                long count = tagRepository.countByFunding(funding);
                if(count < 5) {
                    Tag tag = new Tag(tag_name.getTagName(), funding);
                    tagRepository.save(tag);
                    tag_id = tag.getTag_id();
                }else{
                    return InsertTagResponseDto.full_data();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return InsertTagResponseDto.success(tag_id);
    }

    @Transactional
    public ResponseEntity<? super DeleteDataResponseDto> deleteTag(Long id){
        try{
            //id에 해당하는 값이 없으면 자동으로 EmptyResultDataAccessException 을 발생
            tagRepository.deleteById(id);
        } catch(Exception e){
            e.printStackTrace();
            return DeleteDataResponseDto.not_existed_data();
        }
        return DeleteDataResponseDto.success();
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
                documentRepository.save(document);   //id_Card는 uuid는 안넘겨 줘도 됨.
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
            if(file_path == null || file_path.isEmpty()){
                return DeleteFileResponseDto.not_existed_file();
            }
            if(!fileService.file_delete(file_path)){
                return ResponseDto.databaseError();
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
            if(document == null || document.getFpath() == null || document.getFpath().isEmpty()){
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
