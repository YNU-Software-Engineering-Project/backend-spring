package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.wirtefunding.PaySuccessRequestDto;
import sg.backend.dto.response.writefunding.PaySuccessResponseDto;
import sg.backend.service.PaymentService;

@RequiredArgsConstructor
@RequestMapping("/api/funding")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{funding_id}/pay/success")
    public ResponseEntity<? super PaySuccessResponseDto> paySuccess(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long funding_id,
            @RequestBody @Valid PaySuccessRequestDto requestBody) {
        return paymentService.paySuccess(email, funding_id, requestBody);
    }
}
