package com.sparta.paymentsystem.domain.order.dto;

import java.util.List;

public record CheckoutResponse(
        List<CheckoutItemResponse> items,
        int totalPrice
) {
    public record CheckoutItemResponse(
            Long productId,
            String productName,
            int price,
            int quantity,
            int subtotal
    ) {}
}
/*
        OrderResponse
        → 이미 생성된 주문 정보

        CheckoutResponse
        → 주문하기 직전에 사용자에게 보여줄 주문/결제 예정 정보
 */