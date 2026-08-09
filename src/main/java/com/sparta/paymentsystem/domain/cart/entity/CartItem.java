package com.sparta.paymentsystem.domain.cart.entity;
import com.sparta.paymentsystem.domain.member.entity.Member;
import com.sparta.paymentsystem.domain.product.entity.Product;
import com.sparta.paymentsystem.global.entity.BaseTimeEntity;
import com.sparta.paymentsystem.global.error.BusinessException;
import com.sparta.paymentsystem.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "product_id"})
        //같은 회원이 같은 상품을 장바구니에 중복으로 담아도 (member_id, product_id) 조합당 행이 1개만 존재하도록 DB 레벨에서 중복을 원천 차단하기 위해서입니다.
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "int UNSIGNED DEFAULT 1")
    private int quantity;

    public CartItem(Member member, Product product, int quantity) {
        this.member = member;
        this.product = product;
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    // quantity의 변경은 CartItem만 가능해야한다.
    public void addQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity += quantity;
    }

    public void changeQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
    }
}