package com.example.e_commerce.dto.address;

import jakarta.validation.constraints.NotBlank;

public class AddressUpdateRequest {
    @NotBlank(message = "Adres başlığı boş olamaz")
    private String title;

    @NotBlank(message = "Ad boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    private String lastName;

    @NotBlank(message = "Telefon boş olamaz")
    private String phone;

    @NotBlank(message = "Şehir boş olamaz")
    private String city;

    @NotBlank(message = "İlçe boş olamaz")
    private String district;

    @NotBlank(message = "Mahalle boş olamaz")
    private String neighborhood;

    private String street;

    @NotBlank(message = "Adres satırı boş olamaz")
    private String addressLine;

    private String zipCode;

    private Boolean isDefault;
}
