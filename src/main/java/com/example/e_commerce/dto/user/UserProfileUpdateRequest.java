package com.example.e_commerce.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    @NotBlank(message = "Ad boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    private String lastName;
}
