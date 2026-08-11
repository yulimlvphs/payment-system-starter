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
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getMemberId().equals(memberId))
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

    public void clearCartItems(List<Long> orderedItemIds, Long memberId) {
        int deleted = cartItemRepository.deleteAllByIdInAndMemberId(orderedItemIds, memberId);
        if (deleted != orderedItemIds.size()) {
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