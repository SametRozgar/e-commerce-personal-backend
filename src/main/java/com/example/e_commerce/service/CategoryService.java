package com.example.e_commerce.service;

import com.example.e_commerce.entity.Category;
import com.example.e_commerce.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesWithProducts() {
        return categoryRepository.findAllWithProducts();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    public Category getCategoryWithProducts(Long id) {
        return categoryRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    @Transactional
    public Category createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Bu isimde bir kategori zaten var");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryUpdateRequest request) {
        Category existingCategory = getCategory(id);

        // İsim değiştiyse ve başka bir kategoriyle çakışıyorsa kontrol et
        if (!existingCategory.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Bu isimde bir kategori zaten var");
        }

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());

        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategory(id);

        // Kategoriye ait ürünler varsa silme
        Long productCount = categoryRepository.countProductsInCategory(id);
        if (productCount > 0) {
            throw new RuntimeException("Bu kategoriye ait ürünler olduğu için silinemez. Önce ürünleri silin veya taşıyın.");
        }

        categoryRepository.delete(category);
    }

    public List<Object[]> getCategoryProductCounts() {
        return categoryRepository.findCategoryProductCounts();
    }

    public List<Category> searchCategories(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}