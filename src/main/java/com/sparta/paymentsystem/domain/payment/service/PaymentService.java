package com.sparta.paymentsystem.domain.payment.service;

import com.sparta.paymentsystem.domain.order.entity.Order;
import com.sparta.paymentsystem.domain.payment.entity.Payment;
import com.sparta.paymentsystem.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // 결제 생성
    @Transactional
    public Payment createPayment(Order order, int amount) {
        Payment payment = new Payment(order, amount);
        return paymentRepository.save(payment);
    }

    // 주문 ID(orderId)를 가지고, 그 주문에 연결된 결제의 ID(paymentId)를 찾아라.
    // 주문에서 결제 관련 작업으로 넘어갈 때 필요하다. 예를 들어 사용자가 주문 상세 페이지를 보고 있다가 [결제 취소], [결제 정보 확인] 같은 작업을 하기 위해서는 해당 주문의 Payment를 찾아야함.
    public Optional<Long> findPaymentIdByOrderId(Long orderId) {
        return paymentRepository.findIdByOrderId(orderId);
    }

    // 주문 목록에 결제 ID를 붙이기 위한 "Order → Payment" 조회
    public Map<Long, Long> findPaymentIdMapByOrderIds(List<Long> orderIds) {
        //  IN () 쿼리가 DB로 나가는 걸 차단하고 조기 반환
        if (orderIds.isEmpty()) return Map.of();
        // Repository의 [orderId, paymentId] 튜플(Object[])을 Map<OrderId, PaymentId>로 재구성
        return paymentRepository.findIdsByOrderIds(orderIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

}
