package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Image;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProduct(Product product);

    List<Image> findByProductId(Long productId);

    List<Image> findByProductVariant(ProductVariant productVariant);

    List<Image> findByProductVariantId(Long productVariantId);

    Optional<Image> findByProductIdAndIsPrimaryTrue(Long productId);

    Optional<Image> findByProductVariantIdAndIsPrimaryTrue(Long productVariantId);

    List<Image> findByProductIdOrderByDisplayOrderAsc(Long productId);

    List<Image> findByProductVariantIdOrderByDisplayOrderAsc(Long productVariantId);

    @Modifying
    @Query("UPDATE Image i SET i.isPrimary = false WHERE i.product.id = :productId")
    void clearPrimaryImagesForProduct(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Image i SET i.isPrimary = false WHERE i.productVariant.id = :variantId")
    void clearPrimaryImagesForVariant(@Param("variantId") Long variantId);

    @Modifying
    @Query("UPDATE Image i SET i.isPrimary = true WHERE i.id = :imageId")
    int setImageAsPrimary(@Param("imageId") Long imageId);

    boolean existsByProductIdAndIsPrimaryTrue(Long productId);

    boolean existsByProductVariantIdAndIsPrimaryTrue(Long productVariantId);

    @Query("SELECT COUNT(i) FROM Image i WHERE i.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.productVariant.id = :variantId")
    void deleteByProductVariantId(@Param("variantId") Long variantId);
}
