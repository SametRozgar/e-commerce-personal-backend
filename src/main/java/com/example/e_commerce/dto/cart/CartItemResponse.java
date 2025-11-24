package com.example.e_commerce.dto.cart;

import com.example.e_commerce.dto.product.ProductVariantResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemResponse {
    private Long id;
    private ProductVariantResponse productVariant;
    private Integer quantity;
    private Double itemPrice;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
