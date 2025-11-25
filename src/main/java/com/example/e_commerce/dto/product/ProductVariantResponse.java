package com.example.e_commerce.dto.product;

import com.example.e_commerce.dto.image.ImageResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVariantResponse {
    private Long id;
    private String size;
    private Double price;
    private Integer stock;
    private Double priceMultiplier;
    private List<ImageResponse> images;
    private String primaryImageUrl; // Varyanta özel görsel
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
