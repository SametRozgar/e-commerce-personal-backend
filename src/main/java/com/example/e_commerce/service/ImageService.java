package com.example.e_commerce.service;

import com.example.e_commerce.dto.image.ImageUpdateRequest;
import com.example.e_commerce.dto.image.ImageUploadRequest;
import com.example.e_commerce.entity.Image;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.entity.ProductVariant;
import com.example.e_commerce.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final FileStorageService fileStorageService;

    public List<Image> getProductImages(Long productId) {
        return imageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
    }

    public List<Image> getVariantImages(Long variantId) {
        return imageRepository.findByProductVariantIdOrderByDisplayOrderAsc(variantId);
    }

    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Görsel bulunamadı"));
    }

    @Transactional
    public Image uploadProductImage(Long productId, MultipartFile file, ImageUploadRequest request) {
        Product product = productService.getProduct(productId);

        // Dosyayı yükle
        String imageUrl = fileStorageService.storeFile(file);
        String publicId = generatePublicId();

        // Eğer primary olarak işaretlenmişse, diğer primary'leri false yap
        if (request.getIsPrimary()) {
            imageRepository.clearPrimaryImagesForProduct(productId);
        }

        Image image = Image.builder()
                .imageUrl(imageUrl)
                .publicId(publicId)
                .altText(request.getAltText())
                .displayOrder(request.getDisplayOrder())
                .imageType(Image.ImageType.PRODUCT)
                .product(product)
                .isPrimary(request.getIsPrimary())
                .build();

        return imageRepository.save(image);
    }

    @Transactional
    public Image uploadVariantImage(Long variantId, MultipartFile file, ImageUploadRequest request) {
        ProductVariant variant = productVariantService.getVariant(variantId);

        // Dosyayı yükle
        String imageUrl = fileStorageService.storeFile(file);
        String publicId = generatePublicId();

        // Eğer primary olarak işaretlenmişse, diğer primary'leri false yap
        if (request.getIsPrimary()) {
            imageRepository.clearPrimaryImagesForVariant(variantId);
        }

        Image image = Image.builder()
                .imageUrl(imageUrl)
                .publicId(publicId)
                .altText(request.getAltText())
                .displayOrder(request.getDisplayOrder())
                .imageType(Image.ImageType.VARIANT)
                .productVariant(variant)
                .isPrimary(request.getIsPrimary())
                .build();

        return imageRepository.save(image);
    }

    @Transactional
    public Image updateImage(Long imageId, ImageUpdateRequest request) {
        Image existingImage = getImage(imageId);

        if (request.getAltText() != null) {
            existingImage.setAltText(request.getAltText());
        }

        if (request.getDisplayOrder() != null) {
            existingImage.setDisplayOrder(request.getDisplayOrder());
        }

        // Primary durumu değiştiyse
        if (request.getIsPrimary() != null && request.getIsPrimary()) {
            if (existingImage.getProduct() != null) {
                imageRepository.clearPrimaryImagesForProduct(existingImage.getProduct().getId());
            } else if (existingImage.getProductVariant() != null) {
                imageRepository.clearPrimaryImagesForVariant(existingImage.getProductVariant().getId());
            }
            existingImage.setIsPrimary(true);
        }

        return imageRepository.save(existingImage);
    }

    @Transactional
    public Image setAsPrimary(Long imageId) {
        Image image = getImage(imageId);

        if (image.getProduct() != null) {
            imageRepository.clearPrimaryImagesForProduct(image.getProduct().getId());
        } else if (image.getProductVariant() != null) {
            imageRepository.clearPrimaryImagesForVariant(image.getProductVariant().getId());
        }

        image.setIsPrimary(true);
        return imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        Image image = getImage(imageId);
        String publicId = image.getPublicId();

        // Önce veritabanından sil
        imageRepository.delete(image);

        // Sonra storage'dan sil
        fileStorageService.deleteFile(publicId);
    }

    @Transactional
    public void reorderImages(List<Long> imageIds) {
        for (int i = 0; i < imageIds.size(); i++) {
            Image image = getImage(imageIds.get(i));
            image.setDisplayOrder(i);
            imageRepository.save(image);
        }
    }

    private String generatePublicId() {
        return "img_" + UUID.randomUUID().toString();
    }

    public Long getProductImageCount(Long productId) {
        return imageRepository.countByProductId(productId);
    }
}
