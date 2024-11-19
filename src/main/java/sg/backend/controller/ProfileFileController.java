package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.response.ResponseDto;
import sg.backend.exception.CustomException;
import sg.backend.jwt.CustomPrincipal;
import sg.backend.service.FileService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class ProfileFileController {

    private final FileService fileService;

    @Operation(
            summary = "프로필 이미지 불러오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 이미지 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = CustomException.class)))
    })
    @GetMapping(value = "/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Resource getImage(
            @PathVariable("fileName") String fileName
    ) {
        return fileService.getProfileImage(fileName);
    }

    @Operation(
            summary = "프로필 이미지 삭제하기",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 이미지 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = CustomException.class)))
    })
    @DeleteMapping(value = "/{fileName}")
    public ResponseEntity<ResponseDto> deleteImage(
            @PathVariable("fileName") String fileName,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        String email = principal.getEmail();
        return fileService.deleteProfileImage(fileName, email);
    }
}
