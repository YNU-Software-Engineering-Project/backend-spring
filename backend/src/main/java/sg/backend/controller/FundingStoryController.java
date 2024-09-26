package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.wirtefunding.FundingStoryRequestDto;
import sg.backend.dto.response.writefunding.project.GetProjectResponseDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.file.DeleteFileResponseDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.writefunding.file.UploadImageResponseDto;
import sg.backend.service.implement.FundingStoryService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingStoryController {

    private final FundingStoryService fundingStoryService;

    @Operation(summary = "스토리작성 페이지 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 정보 가져오기 성공",
                    content = @Content(schema = @Schema(implementation = GetProjectResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}/story")
    public ResponseEntity<? super GetProjectResponseDto> get_project(@PathVariable long funding_id) {
        ResponseEntity<? super GetProjectResponseDto> response = fundingStoryService.getProject(funding_id);
        return response;
    }

    @Operation(summary = "프로젝트 제목, 요약 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/story/modify")
    public ResponseEntity<? super ModifyContentResponseDto> modify_project(
            @RequestBody @Valid FundingStoryRequestDto requestBody,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingStoryService.modify_project(funding_id, requestBody);
        return response;
    }

    @Operation(summary = "소개 사진 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하나의 사진 추가 성공",
                    content = @Content(schema = @Schema(implementation = UploadImageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    - 존재하지 않는 funding_id
                    - 추가할 파일이 없을 때
                    - 파일 형식이 맞지 않을 때
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/story/images")
    public ResponseEntity<? super UploadImageResponseDto> upload_images(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadImageResponseDto> response = fundingStoryService.upload_images(funding_id, file, true);
        return response;
    }

    @Operation(summary = "메인 사진 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인사진 추가 성공",
                    content = @Content(schema = @Schema(implementation = UploadImageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 추가할 파일이 없을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/story/main")
    public ResponseEntity<? super UploadImageResponseDto> upload_main(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadImageResponseDto> response = fundingStoryService.upload_images(funding_id, file, false);
        return response;
    }

    @Operation(summary = "메인사진 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인사진 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 삭제할 파일이 없을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/{funding_id}/story/del_main")
    public ResponseEntity<? super DeleteFileResponseDto> delete_main(
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingStoryService.deleteMain(funding_id);
        return response;
    }

    @Operation(summary = "소개사진 하나 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소개사진 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "파일이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/story/image/{uuid_name}")
    public ResponseEntity<? super DeleteFileResponseDto> delete_image(
            @PathVariable("uuid_name") String uuid_name
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingStoryService.deleteImage(uuid_name);
        return response;
    }

}
