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
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "위시리스트 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/wishlist")
    public ResponseEntity<? super GetFundingListResponseDto> getWishList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetFundingListResponseDto> response = userService.getWishList(userId, page, size);
        return response;
    }

    @Operation(
            summary = "참여한 펀딩 리스트 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여한 펀딩 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/pledges")
    public ResponseEntity<? super GetFundingListResponseDto> getPledgeList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetFundingListResponseDto> response = userService.getPledgeList(userId, page, size);
        return response;
    }

    @Operation(
            summary = "내 펀딩 리스트 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 펀딩 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetMyFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings")
    public ResponseEntity<? super GetMyFundingListResponseDto> getMyFundingList(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetMyFundingListResponseDto> response = userService.getMyFundingList(userId, page, size);
        return response;
    }
}
