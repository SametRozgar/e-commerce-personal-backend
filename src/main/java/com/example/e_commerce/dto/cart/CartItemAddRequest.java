package com.example.e_commerce.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemAddRequest {
    @NotNull(message = "Varyant ID boş olamaz")
    private Long variantId;

    @NotNull(message = "Miktar boş olamaz")
    @Positive(message = "Miktar pozitif olmalıdır")
    private Integer quantity;
}
