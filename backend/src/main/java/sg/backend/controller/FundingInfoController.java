package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.FundingInfoRequestDto;
import sg.backend.dto.request.InsertTagRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.file.DeleteFileResponseDto;
import sg.backend.dto.response.file.UploadInfoFileResponseDto;
import sg.backend.dto.response.DeleteDataResponseDto;
import sg.backend.dto.response.fundingInfo.GetInfoResponseDto;
import sg.backend.dto.response.fundingInfo.InsertTagResponseDto;
import sg.backend.service.implement.FundingInfoService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingInfoController {

    private final FundingInfoService fundingInfoService;

    @GetMapping("/{funding_id}")
    public ResponseEntity<? super GetFundingMainResponseDto> getFundingMain(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetFundingMainResponseDto> response = fundingInfoService.getFundingMain(funding_id);
        return response;
    }

    @GetMapping("/{funding_id}/info")
    public ResponseEntity<? super GetInfoResponseDto> getInfo(
            @PathVariable Long funding_id) {
        ResponseEntity<? super GetInfoResponseDto> response = fundingInfoService.getInfo(funding_id);
        return response;
    }

    @PostMapping("/{funding_id}/info/modify")
    public ResponseEntity<? super ModifyContentResponseDto> modifyInfo(
            @RequestBody @Valid FundingInfoRequestDto requestBody,
            @PathVariable("funding_id") Long funding_id
    ) {
        ResponseEntity<? super ModifyContentResponseDto> response = fundingInfoService.modifyInfo(funding_id, requestBody, false);
        return response;
    }

    @PostMapping("/{funding_id}/info/tag")
    public ResponseEntity<? super InsertTagResponseDto> insertTag(
            @PathVariable("funding_id") Long funding_id,
            @RequestBody InsertTagRequestDto requestBody
    ){
        ResponseEntity<? super InsertTagResponseDto> response = fundingInfoService.insertTag(funding_id, requestBody);
        return response;
    }

    @DeleteMapping("/info/{tag_id}/del_tag")
    public ResponseEntity<? super DeleteDataResponseDto> deleteTag(
            @PathVariable("tag_id") Long tag_id
    ){
        ResponseEntity<? super DeleteDataResponseDto> response = fundingInfoService.deleteTag(tag_id);
        return response;
    }

    @PostMapping("/{funding_id}/info/id_card")
    public ResponseEntity<? super UploadInfoFileResponseDto> uploadIDcard(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadInfoFileResponseDto> response = fundingInfoService.uploadFile(funding_id, file, false);
        return response;
    }

    @PostMapping("/{funding_id}/info/document")
    public ResponseEntity<? super UploadInfoFileResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadInfoFileResponseDto> response = fundingInfoService.uploadFile(funding_id, file, true);
        return response;
    }

    @DeleteMapping("/{funding_id}/info/del_id_card")
    public ResponseEntity<? super DeleteFileResponseDto> deleteIDcard(
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingInfoService.deleteIDcard(funding_id);
        return response;
    }

    @DeleteMapping("/info/document/{uuid_name}")
    public ResponseEntity<? super DeleteFileResponseDto> deleteDocument(
            @PathVariable("uuid_name") String uuid_name
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingInfoService.deleteDocument(uuid_name);
        return response;
    }
}
