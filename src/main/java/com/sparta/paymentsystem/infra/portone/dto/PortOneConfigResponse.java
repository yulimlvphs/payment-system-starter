package com.sparta.paymentsystem.infra.portone.dto;

// 브라우저에서 결제창을 열때 아래의 두 값이 반드시 필요하다.
public record PortOneConfigResponse(
        String storeId,
        String channelKey
) {}