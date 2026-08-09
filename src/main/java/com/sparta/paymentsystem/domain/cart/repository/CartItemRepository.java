package com.sparta.paymentsystem.domain.cart.repository;
import com.sparta.paymentsystem.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.member.id = :memberId")
    List<CartItem> findByMemberId(@Param("memberId") Long memberId);

    // 중복 여부 확인
    Optional<CartItem> findByMember_IdAndProduct_Id(Long memberId, Long productId);

    @Modifying //@Query에 작성한 쿼리가 조회용 SELECT가 아니라 데이터를 변경하는 UPDATE·DELETE 쿼리라고 Spring Data JPA에 알려주는 표시
    @Query("DELETE FROM CartItem ci WHERE ci.id = :id AND ci.member.id = :memberId")
    int deleteByIdAndMember_Id(@Param("id") Long id, @Param("memberId") Long memberId);

}
