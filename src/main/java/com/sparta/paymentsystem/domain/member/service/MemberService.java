package com.sparta.paymentsystem.domain.member.service;
import com.sparta.paymentsystem.domain.member.dto.MemberReponse;
import com.sparta.paymentsystem.domain.member.entity.Member;
import com.sparta.paymentsystem.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberReponse getMe(Long memberId) {
        Member member = findById(memberId);
        return new MemberReponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getCreatedAt()
        );
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }
}