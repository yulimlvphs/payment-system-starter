package com.sparta.paymentsystem.domain.auth.dto;

public record AuthResponse(
        String token,
        MemberInfo member
) {
    // 굳이 아래의 클래스를 정의하지 않고 AuthResponse에 모든 필드를 넣어도 되지만 토근, 회원장보를 명확하게 분리하게 위해서다.
    public record MemberInfo(
            Long id,
            String name,
            String email,
            String phoneNumber
    ) {}
}
