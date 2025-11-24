package com.example.e_commerce.dto.address;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressResponse {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String district;
    private String neighborhood;
    private String street;
    private String addressLine;
    private String zipCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
