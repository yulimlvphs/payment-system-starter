package com.sparta.paymentsystem.domain.payment.controller;

import com.sparta.paymentsystem.domain.payment.dto.PaymentCancelRequest;
import com.sparta.paymentsystem.domain.payment.dto.PaymentCancelResponse;
import com.sparta.paymentsystem.domain.payment.dto.PaymentConfirmRequest;
import com.sparta.paymentsystem.domain.payment.dto.PaymentConfirmResponse;
import com.sparta.paymentsystem.domain.payment.facade.PaymentFacade;
import com.sparta.paymentsystem.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    // PortOne 결제창이 성공 결과를 반환했더라도 브라우저의 결과를 그대로 신뢰하면 안 됩니다.
    // 사용자가 개발자 도구나 별도의 프로그램으로 다음 요청을 조작할 수 있기 때문입니다.
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentConfirmResponse>> confirmPayment(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody PaymentConfirmRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(paymentFacade.confirmPayment(memberId, request)));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<PaymentCancelResponse>> cancelPayment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id,
            @Valid @RequestBody(required = false) PaymentCancelRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(paymentFacade.cancelPayment(memberId, id, request)));
    }
}
