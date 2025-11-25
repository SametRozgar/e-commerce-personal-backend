package com.example.e_commerce.dto.cart;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private List<CartItemResponse> cartItems;
    private Double totalPrice;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
