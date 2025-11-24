package com.example.e_commerce.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PriceUpdateRequest {
    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif olmalıdır")
    private Double price;
}