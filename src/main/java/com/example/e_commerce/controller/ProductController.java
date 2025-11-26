package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.product.*;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Ürün yönetimi API'leri")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Tüm ürünleri getir")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Ürünler getirildi"));
    }

    @GetMapping("/page")
    @Operation(summary = "Sayfalı ürün listesi")
    public ResponseEntity<ApiResponse<Page<Product>>> getProductsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products, "Sayfalı ürünler getirildi"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ürün detayını getir")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Ürün getirildi"));
    }

    @GetMapping("/{id}/with-variants")
    @Operation(summary = "Ürün detayını varyantlarıyla getir")
    public ResponseEntity<ApiResponse<Product>> getProductWithVariants(@PathVariable Long id) {
        Product product = productService.getProductWithVariants(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Ürün ve varyantları getirildi"));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Kategoriye göre ürünleri getir")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products, "Kategori ürünleri getirildi"));
    }

    @GetMapping("/search")
    @Operation(summary = "Ürün ara")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(ApiResponse.success(products, "Arama sonuçları getirildi"));
    }

    @GetMapping("/available")
    @Operation(summary = "Stokta olan ürünleri getir")
    public ResponseEntity<ApiResponse<List<Product>>> getAvailableProducts() {
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(ApiResponse.success(products, "Stokta olan ürünler getirildi"));
    }

    @GetMapping("/category/{categoryId}/available")
    @Operation(summary = "Kategoriye göre stokta olan ürünleri getir")
    public ResponseEntity<ApiResponse<List<Product>>> getAvailableProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getAvailableProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products, "Kategoriye göre stokta olan ürünler getirildi"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Yeni ürün oluştur (ADMIN)")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(product, "Ürün başarıyla oluşturuldu"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ürün güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        Product product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(product, "Ürün başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ürün sil (ADMIN)")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ürün başarıyla silindi"));
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ürün istatistikleri (ADMIN)")
    public ResponseEntity<ApiResponse<Long>> getProductCount() {
        Long count = productService.getTotalProductCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Ürün sayısı getirildi"));
    }
}