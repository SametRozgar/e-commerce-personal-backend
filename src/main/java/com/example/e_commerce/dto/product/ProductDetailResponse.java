package com.example.e_commerce.dto.product;

import com.example.e_commerce.dto.category.CategoryResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private List<ProductVariantResponse> variants;
    private Integer totalStock;
    private Double minPrice;
    private Double maxPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
