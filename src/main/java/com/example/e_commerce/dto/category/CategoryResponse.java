package com.example.e_commerce.dto.category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer productCount;
}
