package com.example.e_commerce.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Mevcut şifre boş olamaz")
    private String currentPassword;

    @NotBlank(message = "Yeni şifre boş olamaz")
    private String newPassword;
}
