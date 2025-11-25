package com.example.e_commerce.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderCreateRequest {
    @NotNull(message = "Adres ID boş olamaz")
    private Long addressId;

    private String note;
}
