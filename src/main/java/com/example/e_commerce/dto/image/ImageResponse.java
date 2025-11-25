package com.example.e_commerce.dto.image;

import lombok.Data;

@Data
public class ImageResponse {
    private Long id;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
    private String imageType;
    private String createdAt;
    private String updatedAt;
}
