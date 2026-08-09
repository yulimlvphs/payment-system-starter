package com.sparta.paymentsystem.domain.member.dto;

import java.time.LocalDateTime;

public record MemberReponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
) {
}
