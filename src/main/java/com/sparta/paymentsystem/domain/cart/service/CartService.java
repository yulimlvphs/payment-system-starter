package com.sparta.paymentsystem.domain.cart.service;

import com.sparta.paymentsystem.domain.cart.dto.CartItemResponse;
import com.sparta.paymentsystem.domain.cart.entity.CartItem;
import com.sparta.paymentsystem.domain.cart.repository.CartItemRepository;
import com.sparta.paymentsystem.global.error.BusinessException;
import com.sparta.paymentsystem.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartItemRepository cartItemRepository;

    // 화면/API 응답용 장바구니 목록 조회
    public List<CartItemResponse> getCartItems(Long memberId) {
        return cartItemRepository.findByMemberId(memberId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public Long addItem(CartItem cartItem) {
        // 이미 존재하는 MemberId + ProductId의 조합인지 확인
        Optional<CartItem> existing = cartItemRepository.findByMember_IdAndProduct_Id(
                cartItem.getMemberId(), cartItem.getProductId()
        );
        // 이미 존재하는 조합이라면 해당 값을 담고, 그 값의 수량을 증가시키기
        if (existing.isPresent()) {
            CartItem found = existing.get();
            found.addQuantity(cartItem.getQuantity());
            return found.getId();
        } else {
            return cartItemRepository.save(cartItem).getId();
        }
    }

    @Transactional
    public void updateQuantity(Long memberId, Long itemId, int quantity) {
        // 만약에 item과 memberId를 동시에 일치하는 항목을 처음부터 쿼리로 DB에서 가지고 온다면 별도의 메서드를 하나 더 정의해야하는데 findById는 기본 제공 메서드
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getMemberId().equals(memberId))
                //왜 소유권 불일치도 CART_ITEM_NOT_FOUND일까??
                //다른 회원의 장바구니 상품이 존재하더라도 요청자에게는 없는 것처럼 처리하는 것이 일반적이다.
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        item.changeQuantity(quantity);
    }

    @Transactional
    public void removeItem(Long memberId, Long itemId) {
        int deleted = cartItemRepository.deleteByIdAndMember_Id(itemId, memberId);
        if (deleted == 0) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
    }

    // 회원의 전체 장바구나 엔티티 조회
    public List<CartItem> findCartEntities(Long memberId) {
        return cartItemRepository.findByMemberId(memberId);
    }

    // 주문할 때 선택한 장바구니 엔티티만 조회
    public List<CartItem> findCartEntitiesByIds(Long memberId, List<Long> cartItemIds) {
        return cartItemRepository.findByIdInAndMember_IdWithProduct(cartItemIds, memberId);
    }

    // 주문완료 후 장바구니 비우기
    // 즉, 주문하기 전에:
    // 요청한 장바구니 상품이 모두 존재하는가?, 모두 현재 회원의 것인가?, 주문 가능한 상품인가?, 재고가 충분한가?
    // 를 이미 검증했다면, 주문 후 장바구니 삭제는 부가적인 정리 작업이다.
    public void clearCartItems(List<Long> orderedItemIds, Long memberId) {
        int deleted = cartItemRepository.deleteAllByIdInAndMemberId(orderedItemIds, memberId);
        if (deleted != orderedItemIds.size()) {
            // 오류를 발생시키지 않는 이유는 여기서 오류가 발생하면 이 메서드를 쓰는 CartFacade의 메서드의 트랜젝션이 모두 롤백될 수 있음. + 이 기능은 비주류?의 기능임.
            log.warn("장바구니 삭제 불일치: expected={}, actual={}, memberId={}",
                    orderedItemIds.size(), deleted, memberId);
        }
    }

    // 응답 DTO로 변환
    private CartItemResponse toResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getProduct().getStock()
        );
    }
}