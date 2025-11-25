package com.example.e_commerce.dto.image;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ImageReorderRequest {
    @NotNull
    private List<Long> imageIds;
}
