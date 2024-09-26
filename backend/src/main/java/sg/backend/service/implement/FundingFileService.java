package sg.backend.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FundingFileService {

    @Value("${file.path}")
    private String filePath;

    public String file_upload(String type, MultipartFile file) {

        String originalFilename = file.getOriginalFilename();  //파일 이름 가져오기
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));  //확장자 가져오기
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + extension;  //저장할 새로운 파일 이름

        String savePath = filePath + type + "/" + fileName;

        try{
            file.transferTo(new File(savePath));  //파일을 경로에 저장
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return fileName;
    }

    public Boolean file_delete(String path){

        Path filepath = Paths.get(path);

        try { //파일 삭제시 성공하면 true, 실패 false
            return Files.deleteIfExists(filepath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
