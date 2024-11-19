package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import sg.backend.dto.response.writefunding.GetFundingMainResponseDto;
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


    @Operation(summary = "toast ui 이미지 저장하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버에 이미지 저장 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingMainResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "이미지 저장 실패",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/editor/image-upload")    //story_id를 넘겨줌
    @ResponseBody  // JSON 형식의 응답을 반환
    public ResponseEntity<? super StoryImageResponseDto> uploadEditorImage(
            @RequestParam final MultipartFile image,
            @PathVariable("funding_id") Long funding_id) {
        return postService.file_upload(funding_id, image);
    }

    @Operation(summary = "이미지 url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 보여주기 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingMainResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "이미지 보여주기 실패",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
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


    @Operation(summary = "toast ui 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토리 내용 저장 성고",
                    content = @Content(schema = @Schema(implementation = GetFundingMainResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "스토리 내용 저장 실패",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/editor/save")
    public ResponseEntity<ResponseDto> savePost(@RequestBody @Valid StoryContentDto content,
                                                @PathVariable("funding_id") Long funding_id) {
        return postService.savePost(funding_id, content.getContent());
    }

}
