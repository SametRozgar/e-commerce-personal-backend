package com.example.e_commerce.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockUpdateRequest {
    @NotNull(message = "Stok miktarı boş olamaz")
    @Positive(message = "Stok miktarı pozitif olmalıdır")
    private Integer stock;
}
