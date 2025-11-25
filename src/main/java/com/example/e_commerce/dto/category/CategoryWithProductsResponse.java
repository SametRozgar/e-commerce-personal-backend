package com.example.e_commerce.dto.category;

import com.example.e_commerce.dto.product.ProductResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryWithProductsResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductResponse> products;
}
