package com.example.e_commerce.dto.product;

import com.example.e_commerce.dto.category.CategoryResponse;
import com.example.e_commerce.dto.image.ImageResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private List<ProductVariantResponse> variants;
    private List<ImageResponse> images;
    private String primaryImageUrl; // Hızlı erişim için
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
