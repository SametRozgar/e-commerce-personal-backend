package com.example.e_commerce.repository;


import com.example.e_commerce.entity.Category;
import com.example.e_commerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryId(Long categoryId);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String name);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id")
    Optional<Product> findByIdWithVariants(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.category.id = :categoryId")
    List<Product> findByCategoryIdWithVariants(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants v WHERE v.stock > 0")
    List<Product> findAllWithAvailableVariants();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id IN :productIds")
    List<Product> findByIdsWithVariants(@Param("productIds") List<Long> productIds);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants v WHERE p.category.id = :categoryId AND v.stock > 0")
    List<Product> findAvailableProductsByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(p) FROM Product p")
    Long countAllProducts();

    @Query("SELECT p.category.name, COUNT(p) FROM Product p GROUP BY p.category.id, p.category.name")
    List<Object[]> findProductCountByCategory();
}