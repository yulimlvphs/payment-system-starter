package com.sparta.paymentsystem.domain.order.dto;

import java.util.List;

public record OrderCheckoutRequest(
        List<Long> cartItemIds
) {
    // record에서는 이렇게 매개변수 부분을 생략한 생성자(Compact Constructor)를 작성할 수 있어.
    public OrderCheckoutRequest {
        if (cartItemIds == null) {
            cartItemIds = List.of();
        }
    }
}
