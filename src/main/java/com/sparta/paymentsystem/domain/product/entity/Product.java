package com.sparta.paymentsystem.domain.product.entity;

import com.sparta.paymentsystem.global.entity.BaseTimeEntity;
import com.sparta.paymentsystem.global.error.BusinessException;
import com.sparta.paymentsystem.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    //4바이트 정수에서 음수를 허용하지 않아 값 범위가 0부터 4,294,967,295까지로 늘어나며, 음수가 의미 없는 수량·재고·ID 등에 주로 사용
    @Column(nullable = false, columnDefinition = "int UNSIGNED")
    private int price;

    @Column(nullable = false, columnDefinition = "int UNSIGNED DEFAULT 0")
    private int stock = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Product(String name, int price, int stock, String description) {
        if (price < 0) {
            throw new BusinessException(ErrorCode.INVALID_PRICE);

        }
        if (stock < 0) {
            throw new BusinessException(ErrorCode.INVALID_STOCK);
        }
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

}
