package com.example.e_commerce.repository;

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
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProduct(Product product);

    List<ProductVariant> findByProductId(Long productId);

    Optional<ProductVariant> findByProductIdAndSize(Long productId, String size);

    List<ProductVariant> findByStockGreaterThan(Integer stock);

    List<ProductVariant> findByStockLessThanEqual(Integer stock);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId AND pv.stock > 0")
    List<ProductVariant> findAvailableVariantsByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.stock = pv.stock - :quantity WHERE pv.id = :variantId AND pv.stock >= :quantity")
    int decreaseStock(@Param("variantId") Long variantId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.stock = pv.stock + :quantity WHERE pv.id = :variantId")
    int increaseStock(@Param("variantId") Long variantId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.price = :price WHERE pv.id = :variantId")
    int updatePrice(@Param("variantId") Long variantId, @Param("price") Double price);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.priceMultiplier = :multiplier WHERE pv.id = :variantId")
    int updatePriceMultiplier(@Param("variantId") Long variantId, @Param("multiplier") Double multiplier);

    @Query("SELECT SUM(pv.stock) FROM ProductVariant pv WHERE pv.product.id = :productId")
    Long getTotalStockByProductId(@Param("productId") Long productId);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.stock = 0")
    List<ProductVariant> findOutOfStockVariants();


}
