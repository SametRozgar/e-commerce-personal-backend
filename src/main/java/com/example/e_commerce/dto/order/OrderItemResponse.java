package com.example.e_commerce.dto.order;

import com.example.e_commerce.dto.product.ProductVariantResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemResponse {
    private Long id;
    private ProductVariantResponse productVariant;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
