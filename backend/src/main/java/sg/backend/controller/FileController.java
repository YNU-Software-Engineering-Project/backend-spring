package sg.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.service.implement.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.file_upload("funding_image", file);

        if (fileName == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }

        // 파일을 확인할 수 있는 URL을 반환
        String fileUrl = "http://localhost:8080/file/view/funding_image/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping("/view/funding_image/{file_name}")
    public ResponseEntity<Resource> viewImage(@PathVariable("file_name") String fileName) {
        try {
            String fileDir = "/app/data/funding_image/";
            Path path = Paths.get(fileDir, fileName);
            Resource resource = new UrlResource(path.toUri());

            // 파일의 MIME 타입을 동적으로 결정
            String mimeType = Files.probeContentType(path);

            // MIME 타입이 null일 경우 기본값을 설정할 수 있습니다.
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))  //동적으로 타입 설정 가능
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/view/project_document/{file_name}")
    public ResponseEntity<Resource> viewDocument(@PathVariable("file_name") String file_name){
        try{
            String fileDir = "/app/data/project_document/";
            Path path = Paths.get(fileDir, file_name);
            //경로로 파일 가져오기
            Resource resource = new UrlResource(path.toUri());

            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file_name + "\";") //파일 다운로드
                    .body(resource);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
