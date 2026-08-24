package com.sparta.paymentsystem.infra.portone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class PortOneConfig {

    private final PortOneProperties properties;

    @Bean
    public RestClient portOneRestClient() {
        // 외부 요청은 안정성이 떨어지기 때문에 이를 더 안전하게 관리하기 위한 클래스로.
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);  // 3초 : 연경을 3초 안에 하지 못하면 싪패처리
        requestFactory.setReadTimeout(5000);     // 5초 : 응답을 5초 안에 못받으면 중단해서 요청이 무한정 대기 상태가 되지 않게 함.

        // 앞으로 우리가 포트원 RestClient를 사용할 때마다 이 디폴트 세팅으로 클라이언트가 만들어진 것을 사용하 것임.
        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "PortOne " + properties.getApiSecret())
                .build();
    }
}
