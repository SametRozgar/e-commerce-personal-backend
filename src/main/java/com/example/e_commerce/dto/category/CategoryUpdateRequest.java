package com.example.e_commerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryUpdateRequest {
    @NotBlank(message = "Kategori adı boş olamaz")
    private String name;

    private String description;
}
