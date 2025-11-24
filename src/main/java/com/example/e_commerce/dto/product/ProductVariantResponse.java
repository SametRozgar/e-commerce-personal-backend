package com.example.e_commerce.dto.product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductVariantResponse {
    private Long id;
    private String size;
    private Double price;
    private Integer stock;
    private Double priceMultiplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
