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
import sg.backend.dto.request.wirtefunding.FundingInfoRequestDto;
import sg.backend.dto.request.wirtefunding.InsertTagRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.writefunding.GetFundingMainResponseDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.writefunding.file.DeleteFileResponseDto;
import sg.backend.dto.response.writefunding.file.UploadInfoFileResponseDto;
import sg.backend.dto.response.writefunding.DeleteDataResponseDto;
import sg.backend.dto.response.writefunding.project.GetInfoResponseDto;
import sg.backend.dto.response.writefunding.project.InsertTagResponseDto;
import sg.backend.service.FundingInfoService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingInfoController {

    private final FundingInfoService fundingInfoService;

    @Operation(summary = "메인 사진 보여주기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보여주기 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingMainResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}")
    public ResponseEntity<? super GetFundingMainResponseDto> getFundingMain(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetFundingMainResponseDto> response = fundingInfoService.getFundingMain(funding_id);
        return response;
    }

    @Operation(summary = "프로젝트정보작성 페이지 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}/info")
    public ResponseEntity<? super GetInfoResponseDto> getInfo(
            @PathVariable Long funding_id) {
        ResponseEntity<? super GetInfoResponseDto> response = fundingInfoService.getInfo(funding_id);
        return response;
    }

    @Operation(summary = "프로젝트정보 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    -funding_id가 존재하지 않는 경우
                    - 이메일 형식이 맞지 않는 경우
                    - 날짜 형식이 맞지 않는 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/info/modify")
    public ResponseEntity<ResponseDto> modifyInfo(
            @RequestBody @Valid FundingInfoRequestDto requestBody,
            @PathVariable("funding_id") Long funding_id
    ) {
        ResponseEntity<ResponseDto> response = fundingInfoService.modifyInfo(funding_id, requestBody, null, false);
        return response;
    }

    @Operation(summary = "tag 추가하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "tag 추가 성공",
                    content = @Content(schema = @Schema(implementation = InsertTagResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    - funding_id가 존재하지 않을 때
                    - 추가할 태그가 null일 때
                    - 태그를 5개 이상 추가했을 때
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/info/tag")
    public ResponseEntity<? super InsertTagResponseDto> insertTag(
            @PathVariable("funding_id") Long funding_id,
            @RequestBody InsertTagRequestDto requestBody
    ){
        ResponseEntity<? super InsertTagResponseDto> response = fundingInfoService.insertTag(funding_id, requestBody);
        return response;
    }

    @Operation(summary = "tag 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "tag 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteDataResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제할 태그 id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/info/{tag_id}/del_tag")
    public ResponseEntity<? super DeleteDataResponseDto> deleteTag(
            @PathVariable("tag_id") Long tag_id
    ){
        ResponseEntity<? super DeleteDataResponseDto> response = fundingInfoService.deleteTag(tag_id);
        return response;
    }

    @Operation(summary = "신분증 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신분증 추가 성공",
                    content = @Content(schema = @Schema(implementation = UploadInfoFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 추가할 파일이 없을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/info/id_card")
    public ResponseEntity<? super UploadInfoFileResponseDto> uploadIDcard(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadInfoFileResponseDto> response = fundingInfoService.uploadFile(funding_id, file, false);
        return response;
    }

    @Operation(summary = "심사서류 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "심사서류 추가 성공",
                    content = @Content(schema = @Schema(implementation = UploadInfoFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 추가할 파일이 없을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/info/document")
    public ResponseEntity<? super UploadInfoFileResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadInfoFileResponseDto> response = fundingInfoService.uploadFile(funding_id, file, true);
        return response;
    }

    @Operation(summary = "신분증 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신분증 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 삭제할 파일이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/{funding_id}/info/del_id_card")
    public ResponseEntity<? super DeleteFileResponseDto> deleteIDcard(
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingInfoService.deleteIDcard(funding_id);
        return response;
    }

    @Operation(summary = "심사서류 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "심사서류 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteFileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제할 파일(uuid)이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/info/document/{uuid_name}")
    public ResponseEntity<? super DeleteFileResponseDto> deleteDocument(
            @PathVariable("uuid_name") String uuid_name
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingInfoService.deleteDocument(uuid_name);
        return response;
    }
}
