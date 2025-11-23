package com.example.e_commerce.service;

import com.example.e_commerce.entity.Category;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    public Product getProductWithVariants(Long id) {
        return productRepository.findByIdWithVariants(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsByCategoryWithVariants(Long categoryId) {
        return productRepository.findByCategoryIdWithVariants(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAllWithAvailableVariants();
    }

    public List<Product> getAvailableProductsByCategory(Long categoryId) {
        return productRepository.findAvailableProductsByCategory(categoryId);
    }

    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductUpdateRequest request) {
        Product existingProduct = getProduct(id);
        Category category = categoryService.getCategory(request.getCategoryId());

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    public Long getTotalProductCount() {
        return productRepository.countAllProducts();
    }

    public List<Object[]> getProductCountByCategory() {
        return productRepository.findProductCountByCategory();
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        return productRepository.findByIdsWithVariants(productIds);
    }
}
