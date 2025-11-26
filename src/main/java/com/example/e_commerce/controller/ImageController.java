package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.image.ImageUpdateRequest;
import com.example.e_commerce.dto.image.ImageUploadRequest;
import com.example.e_commerce.entity.Image;
import com.example.e_commerce.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "Görsel yönetimi API'leri")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ürün görseli yükle (ADMIN)")
    public ResponseEntity<ApiResponse<Image>> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "displayOrder", defaultValue = "0") Integer displayOrder,
            @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary) {

        // DTO kullanarak oluştur
        ImageUploadRequest request = new ImageUploadRequest();
        request.setAltText(altText);
        request.setDisplayOrder(displayOrder);
        request.setIsPrimary(isPrimary);

        Image image = imageService.uploadProductImage(productId, file, request);
        return ResponseEntity.ok(ApiResponse.success(image, "Görsel başarıyla yüklendi"));
    }

    @PostMapping("/variants/{variantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ürün varyantı görseli yükle (ADMIN)")
    public ResponseEntity<ApiResponse<Image>> uploadVariantImage(
            @PathVariable Long variantId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText,
            @RequestParam(value = "displayOrder", defaultValue = "0") Integer displayOrder,
            @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary) {

        // DTO kullanarak oluştur
        ImageUploadRequest request = new ImageUploadRequest();
        request.setAltText(altText);
        request.setDisplayOrder(displayOrder);
        request.setIsPrimary(isPrimary);

        Image image = imageService.uploadVariantImage(variantId, file, request);
        return ResponseEntity.ok(ApiResponse.success(image, "Varyant görseli başarıyla yüklendi"));
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Ürün görsellerini getir")
    public ResponseEntity<ApiResponse<List<Image>>> getProductImages(@PathVariable Long productId) {
        List<Image> images = imageService.getProductImages(productId);
        return ResponseEntity.ok(ApiResponse.success(images, "Ürün görselleri getirildi"));
    }

    @GetMapping("/variants/{variantId}")
    @Operation(summary = "Varyant görsellerini getir")
    public ResponseEntity<ApiResponse<List<Image>>> getVariantImages(@PathVariable Long variantId) {
        List<Image> images = imageService.getVariantImages(variantId);
        return ResponseEntity.ok(ApiResponse.success(images, "Varyant görselleri getirildi"));
    }

    @PutMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Görsel bilgilerini güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<Image>> updateImage(
            @PathVariable Long imageId,
            @Valid @RequestBody ImageUpdateRequest request) {
        Image image = imageService.updateImage(imageId, request);
        return ResponseEntity.ok(ApiResponse.success(image, "Görsel bilgileri güncellendi"));
    }

    @PutMapping("/{imageId}/set-primary")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Görseli primary yap (ADMIN)")
    public ResponseEntity<ApiResponse<Image>> setImageAsPrimary(@PathVariable Long imageId) {
        Image image = imageService.setAsPrimary(imageId);
        return ResponseEntity.ok(ApiResponse.success(image, "Görsel primary olarak ayarlandı"));
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Görsel sil (ADMIN)")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(ApiResponse.success(null, "Görsel başarıyla silindi"));
    }
}