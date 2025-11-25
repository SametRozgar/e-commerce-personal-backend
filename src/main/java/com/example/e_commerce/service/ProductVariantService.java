package com.example.e_commerce.service;

import com.example.e_commerce.dto.product.ProductVariantCreateRequest;
import com.example.e_commerce.dto.product.ProductVariantUpdateRequest;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.entity.ProductVariant;
import com.example.e_commerce.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductService productService;

    public List<ProductVariant> getVariantsByProduct(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }

    public List<ProductVariant> getAvailableVariantsByProduct(Long productId) {
        return productVariantRepository.findAvailableVariantsByProductId(productId);
    }

    public ProductVariant getVariant(Long variantId) {
        return productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }

    public ProductVariant getVariantByProductAndSize(Long productId, String size) {
        return productVariantRepository.findByProductIdAndSize(productId, size)
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }

    @Transactional
    public ProductVariant createVariant(ProductVariantCreateRequest request) {
        Product product = productService.getProduct(request.getProductId());

        // Aynı ürün ve beden kombinasyonu kontrolü
        if (productVariantRepository.findByProductIdAndSize(request.getProductId(), request.getSize()).isPresent()) {
            throw new RuntimeException("Bu ürün için aynı bedende varyant zaten mevcut");
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .size(request.getSize())
                .price(request.getPrice())
                .stock(request.getStock())
                .priceMultiplier(request.getPriceMultiplier())
                .build();

        return productVariantRepository.save(variant);
    }

    @Transactional
    public ProductVariant updateVariant(Long variantId, ProductVariantUpdateRequest request) {
        ProductVariant existingVariant = getVariant(variantId);

        // Eğer beden değiştiyse, aynı ürün içinde benzersiz olmalı
        if (!existingVariant.getSize().equals(request.getSize())) {
            productVariantRepository.findByProductIdAndSize(
                    existingVariant.getProduct().getId(), request.getSize()
            ).ifPresent(v -> {
                throw new RuntimeException("Bu ürün için aynı bedende varyant zaten mevcut");
            });
        }

        existingVariant.setSize(request.getSize());
        existingVariant.setPrice(request.getPrice());
        existingVariant.setStock(request.getStock());
        existingVariant.setPriceMultiplier(request.getPriceMultiplier());

        return productVariantRepository.save(existingVariant);
    }

    @Transactional
    public void deleteVariant(Long variantId) {
        ProductVariant variant = getVariant(variantId);
        productVariantRepository.delete(variant);
    }

    @Transactional
    public void decreaseStock(Long variantId, Integer quantity) {
        int updatedRows = productVariantRepository.decreaseStock(variantId, quantity);
        if (updatedRows == 0) {
            throw new RuntimeException("Stok yetersiz veya ürün varyantı bulunamadı");
        }
    }

    @Transactional
    public void increaseStock(Long variantId, Integer quantity) {
        int updatedRows = productVariantRepository.increaseStock(variantId, quantity);
        if (updatedRows == 0) {
            throw new RuntimeException("Ürün varyantı bulunamadı");
        }
    }

    @Transactional
    public ProductVariant updatePrice(Long variantId, Double price) {
        int updatedRows = productVariantRepository.updatePrice(variantId, price);
        if (updatedRows == 0) {
            throw new RuntimeException("Ürün varyantı bulunamadı");
        }
        return getVariant(variantId);
    }

    @Transactional
    public ProductVariant updatePriceMultiplier(Long variantId, Double multiplier) {
        int updatedRows = productVariantRepository.updatePriceMultiplier(variantId, multiplier);
        if (updatedRows == 0) {
            throw new RuntimeException("Ürün varyantı bulunamadı");
        }
        return getVariant(variantId);
    }

    public List<ProductVariant> getOutOfStockVariants() {
        return productVariantRepository.findOutOfStockVariants();
    }

    public List<ProductVariant> getLowStockVariants(Integer threshold) {
        return productVariantRepository.findByStockLessThanEqual(threshold);
    }

    public Long getTotalStockByProduct(Long productId) {
        return productVariantRepository.getTotalStockByProductId(productId);
    }

    public boolean isVariantAvailable(Long variantId, Integer quantity) {
        return productVariantRepository.findById(variantId)
                .map(variant -> variant.getStock() >= quantity)
                .orElse(false);
    }
}