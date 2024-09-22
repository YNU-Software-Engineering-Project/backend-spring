package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.FundingStoryRequestDto;
import sg.backend.dto.response.file.DeleteFileResponseDto;
import sg.backend.dto.response.ModifyContentResponseDto;
import sg.backend.dto.response.file.UploadImageResponseDto;
import sg.backend.service.implement.FundingStoryService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingStoryController {

    private final FundingStoryService fundingStoryService;

    @PostMapping("/{funding_id}/story")
    public ResponseEntity<? super ModifyContentResponseDto> modify_project(
            @RequestBody @Valid FundingStoryRequestDto requestBody,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingStoryService.modify_project(funding_id, requestBody);
        return response;
    }

    @PostMapping("/{funding_id}/story/images")
    public ResponseEntity<? super UploadImageResponseDto> upload_images(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadImageResponseDto> response = fundingStoryService.upload_images(funding_id, file, true);
        return response;
    }

    @PostMapping("/{funding_id}/story/main")
    public ResponseEntity<? super UploadImageResponseDto> upload_main(
            @RequestParam("file") MultipartFile file,
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super UploadImageResponseDto> response = fundingStoryService.upload_images(funding_id, file, false);
        return response;
    }

    @DeleteMapping("/{funding_id}/story/del_main")
    public ResponseEntity<? super DeleteFileResponseDto> delete_main(
            @PathVariable("funding_id") Long funding_id
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingStoryService.deleteMain(funding_id);
        return response;
    }

    @DeleteMapping("/story/image/{uuid_name}")
    public ResponseEntity<? super DeleteFileResponseDto> delete_image(
            @PathVariable("uuid_name") String uuid_name
    ){
        ResponseEntity<? super DeleteFileResponseDto> response = fundingStoryService.deleteImage(uuid_name);
        return response;
    }

}
