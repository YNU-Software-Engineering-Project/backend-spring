package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.wirtefunding.StoryContentDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.project.StoryImageResponseDto;
import sg.backend.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class MarkdownController {

    private final PostService postService;


    @PostMapping("/{funding_id}/editor/image-upload")    //story_id를 넘겨줌
    @ResponseBody  // JSON 형식의 응답을 반환
    public ResponseEntity<? super StoryImageResponseDto> uploadEditorImage(
            @RequestParam final MultipartFile image,
            @PathVariable("funding_id") Long funding_id) {
        return postService.file_upload(funding_id, image);
    }


    @GetMapping(value = "/editor/image-print/{filename}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    })
    public ResponseEntity<byte[]> printEditorImage(@PathVariable("filename") String filename) {
        // 파일 경로 설정
        String fileFullPath = Paths.get("/app/data/story_image/", filename).toString();
        File uploadedFile = new File(fileFullPath);

        if (!uploadedFile.exists()) {
            throw new RuntimeException("파일이 존재하지 않습니다");
        }

        try {
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());

            // 파일 확장자에 맞춰 Content-Type 설정
            HttpHeaders headers = new HttpHeaders();
            String contentType = Files.probeContentType(uploadedFile.toPath());
            headers.setContentType(MediaType.parseMediaType(contentType));

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("이미지를 읽는 중 오류 발생", e);
        }
    }


    // 게시글 상세정보 조회
    @GetMapping("/{funding_id}/editor")
    @ResponseBody  // JSON 형식의 응답을 반환
    public String findPostById(@PathVariable final Long funding_id) {
        return postService.findPostById(funding_id);
    }


    // 게시글 저장
    @PostMapping("/{funding_id}/editor/save")
    public ResponseEntity<ResponseDto> savePost(@RequestBody @Valid StoryContentDto content,
                                                @PathVariable("funding_id") Long funding_id) {
        return postService.savePost(funding_id, content.getContent());
    }

}
