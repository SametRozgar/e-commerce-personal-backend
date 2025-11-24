package com.example.e_commerce.dto.product;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private String name;
    private Long categoryId;
    private Boolean inStockOnly = false;
    private Double minPrice;
    private Double maxPrice;
}
