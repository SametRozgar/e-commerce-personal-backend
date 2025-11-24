package com.example.e_commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductVariantCreateRequest {
    @NotNull(message = "Ürün ID boş olamaz")
    private Long productId;

    @NotBlank(message = "Beden/boyut boş olamaz")
    private String size;

    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif olmalıdır")
    private Double price;

    @NotNull(message = "Stok boş olamaz")
    @Positive(message = "Stok pozitif olmalıdır")
    private Integer stock;

    @NotNull(message = "Fiyat çarpanı boş olamaz")
    @Positive(message = "Fiyat çarpanı pozitif olmalıdır")
    private Double priceMultiplier = 1.0;
}
