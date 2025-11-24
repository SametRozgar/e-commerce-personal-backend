package com.example.e_commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductUpdateRequest {
    @NotBlank(message = "Ürün adı boş olamaz")
    private String name;

    private String description;

    @NotNull(message = "Kategori ID boş olamaz")
    private Long categoryId;
}
