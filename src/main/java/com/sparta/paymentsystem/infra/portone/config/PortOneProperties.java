package com.sparta.paymentsystem.infra.portone.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// application.yaml에 있는 portone: 이라고 되어있는 부분을 읽어서 가지고 옴.
// 해당 값들을 가지고 와서 값들을 채우고, 객체로까지 만들어준다.
@ConfigurationProperties(prefix = "portone")
@Setter
@Getter
public class PortOneProperties {
    private String baseUrl;
    private String apiSecret;
    private String storeId;
    private String channelKey;
    private String webhookSecret;
}
