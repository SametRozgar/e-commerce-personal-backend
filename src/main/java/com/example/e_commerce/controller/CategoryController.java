package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.category.CategoryCreateRequest;
import com.example.e_commerce.dto.category.CategoryUpdateRequest;
import com.example.e_commerce.entity.Category;
import com.example.e_commerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Kategori yönetimi API'leri")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Tüm kategorileri getir")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Kategoriler getirildi"));
    }

    @GetMapping("/with-products")
    @Operation(summary = "Ürünleriyle birlikte kategorileri getir")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategoriesWithProducts() {
        List<Category> categories = categoryService.getAllCategoriesWithProducts();
        return ResponseEntity.ok(ApiResponse.success(categories, "Kategoriler ve ürünler getirildi"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kategori detayını getir")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Kategori getirildi"));
    }

    @GetMapping("/{id}/with-products")
    @Operation(summary = "Kategori detayını ürünleriyle getir")
    public ResponseEntity<ApiResponse<Category>> getCategoryWithProducts(@PathVariable Long id) {
        Category category = categoryService.getCategoryWithProducts(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Kategori ve ürünleri getirildi"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Yeni kategori oluştur (ADMIN)")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(category, "Kategori başarıyla oluşturuldu"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kategori güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        Category category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(category, "Kategori başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kategori sil (ADMIN)")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Kategori başarıyla silindi"));
    }

    @GetMapping("/search")
    @Operation(summary = "Kategori ara")
    public ResponseEntity<ApiResponse<List<Category>>> searchCategories(@RequestParam String name) {
        List<Category> categories = categoryService.searchCategories(name);
        return ResponseEntity.ok(ApiResponse.success(categories, "Arama sonuçları getirildi"));
    }

    @GetMapping("/stats/counts")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kategori-ürün istatistikleri (ADMIN)")
    public ResponseEntity<ApiResponse<List<Object[]>>> getCategoryProductCounts() {
        List<Object[]> counts = categoryService.getCategoryProductCounts();
        return ResponseEntity.ok(ApiResponse.success(counts, "Kategori istatistikleri getirildi"));
    }
}