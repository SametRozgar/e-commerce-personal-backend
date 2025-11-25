package com.example.e_commerce.dto.image;

import lombok.Data;

@Data
public class ImageUploadRequest {
    private String altText;
    private Integer displayOrder = 0;
    private Boolean isPrimary = false;
}
