package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetUserWishListResponseDto;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @Operation(
            summary = "위시리스트 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetUserWishListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("wishlist")
    public ResponseEntity<? super GetUserWishListResponseDto> getNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetUserWishListResponseDto> response = userService.getWishList(userId, page, size);
        return response;
    }

}
