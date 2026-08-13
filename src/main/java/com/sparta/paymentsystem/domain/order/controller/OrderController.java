package com.sparta.paymentsystem.domain.order.controller;

import com.sparta.paymentsystem.domain.order.dto.CheckoutResponse;
import com.sparta.paymentsystem.domain.order.dto.OrderCheckoutRequest;
import com.sparta.paymentsystem.domain.order.dto.OrderCheckoutResponse;
import com.sparta.paymentsystem.domain.order.dto.OrderResponse;
import com.sparta.paymentsystem.domain.order.facade.OrderFacade;
import com.sparta.paymentsystem.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    // 주문서 조회 (GET 결과는 화면을 보여주기 위한 예상 정보)
    // 장바구니 → 주문하기(checkout)
    // 주문하기 전에 주문 예정 내용을 보여준다.
    @GetMapping("/checkout")
    public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(
            @AuthenticationPrincipal Long memberId,
            // cartItemIds는 비어서 올 수도 있음
            @RequestParam(required = false) List<Long> cartItemIds
    ) {
        return ResponseEntity.ok(ApiResponse.ok(orderFacade.getCheckout(memberId, cartItemIds)));
    }

    // 확인한 내용을 바탕으로 실제 주문을 생성한다.
    // (POST 시점의 검증이 실제 주문을 확정하는 최종 검증)
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderCheckoutResponse>> createOrder(
            @AuthenticationPrincipal Long memberId,
            @RequestBody(required = false) OrderCheckoutRequest request) {
        OrderCheckoutResponse response = orderFacade.createOrder(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
    }
    /*
    왜 POST에서도 다시 검증해야 할까?
    GET 요청에서 이미 검증했어도 POST 요청에서 다시 검증해야 해.
    GET으로 주문서를 본 시점에는 재고가 10개였지만, 사용자가 내용을 확인하는 동안 다른 사람이 구매할 수 있다.

    10:00:00 GET /checkout
    → 재고 1개 확인

    10:00:05 다른 사용자가 마지막 1개 주문
    → 재고 0개

    10:00:10 POST /checkout
    → 재고를 다시 검사하지 않으면 잘못된 주문 생성
     */

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(@AuthenticationPrincipal Long memberId) {
        return ResponseEntity.ok(ApiResponse.ok(orderFacade.getOrders(memberId)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@AuthenticationPrincipal Long memberId,
                                                               @PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.ok(orderFacade.getOrder(memberId, orderId)));
    }

}
