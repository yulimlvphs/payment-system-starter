package com.sparta.paymentsystem.infra.portone.controller;

import com.sparta.paymentsystem.global.response.ApiResponse;
import com.sparta.paymentsystem.infra.portone.config.PortOneProperties;
import com.sparta.paymentsystem.infra.portone.dto.PortOneConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortOneConfigController {

    private final PortOneProperties portOneProperties;

    // API는 결제를 수행하는 API가 아니라, 프론트엔드에 PortOne 결제창을 실행하는 데 필요한 설정값을 전달하는 API
    // 이 요청의 목적 자체가 다음 두 값을 조회하는 것
    // 왜 조회하냐? 결제 요청때 필요한 값들을 프론트에서 보이지 않게 변수로 처리하려고
    @GetMapping("/api/config/portone")
    public ResponseEntity<ApiResponse<PortOneConfigResponse>> getConfig() {
        return ResponseEntity.ok(ApiResponse.ok(new PortOneConfigResponse(
                portOneProperties.getStoreId(),
                portOneProperties.getChannelKey()
        )));
    }
}