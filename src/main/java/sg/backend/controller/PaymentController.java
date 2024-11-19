package sg.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.PaymentEmailResponseDto;
import sg.backend.jwt.CustomPrincipal;
import sg.backend.service.PaymentService;

@RequiredArgsConstructor
@RequestMapping("/api/funding")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 버튼 누르기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일, 전화번호, 주소 존재",
                content = @Content(schema = @Schema(implementation = PaymentEmailResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "이메일, 전화번호, 주소가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/pay")
    public ResponseEntity<? super PaymentEmailResponseDto> paymentEmail(@AuthenticationPrincipal CustomPrincipal principal) {
        Long user_id = principal.getUserId();
        if(user_id == null ) {
            return ResponseDto.noPermission();
        }
        return paymentService.paymentEmail(user_id);
    }

}