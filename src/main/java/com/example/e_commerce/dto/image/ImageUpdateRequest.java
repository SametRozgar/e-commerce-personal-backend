package com.example.e_commerce.dto.image;

import lombok.Data;

@Data
public class ImageUpdateRequest {
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
}
