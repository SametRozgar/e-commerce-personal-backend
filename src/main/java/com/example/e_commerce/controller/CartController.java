package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.cart.CartItemAddRequest;
import com.example.e_commerce.dto.cart.CartItemUpdateRequest;
import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.CartItem;
import com.example.e_commerce.service.AuthService;
import com.example.e_commerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "Sepet yönetimi API'leri")
public class CartController {

    private final CartService cartService;
    private final AuthService authService; // AuthService eklendi

    @GetMapping
    @Operation(summary = "Kullanıcının sepetini getir")
    public ResponseEntity<ApiResponse<Cart>> getCart() {
        Long userId = getCurrentUserId();
        Cart cart = cartService.getCartWithItems(userId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Sepet getirildi"));
    }

    @PostMapping("/items")
    @Operation(summary = "Sepete ürün ekle")
    public ResponseEntity<ApiResponse<CartItem>> addItemToCart(@Valid @RequestBody CartItemAddRequest request) {
        Long userId = getCurrentUserId();
        CartItem cartItem = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(ApiResponse.success(cartItem, "Ürün sepete eklendi"));
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Sepetteki ürün miktarını güncelle")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        Long userId = getCurrentUserId();
        CartItem cartItem = cartService.updateCartItemQuantity(userId, cartItemId, request);
        return ResponseEntity.ok(ApiResponse.success(cartItem, "Ürün miktarı güncellendi"));
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Sepetten ürün çıkar")
    public ResponseEntity<ApiResponse<Void>> removeItemFromCart(@PathVariable Long cartItemId) {
        Long userId = getCurrentUserId();
        cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(ApiResponse.success(null, "Ürün sepetten çıkarıldı"));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Sepeti temizle")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Sepet temizlendi"));
    }

    @GetMapping("/total")
    @Operation(summary = "Sepet toplam tutarını getir")
    public ResponseEntity<ApiResponse<Double>> getCartTotal() {
        Long userId = getCurrentUserId();
        Double total = cartService.calculateCartTotal(userId);
        return ResponseEntity.ok(ApiResponse.success(total, "Sepet toplamı getirildi"));
    }

    @GetMapping("/item-count")
    @Operation(summary = "Sepetteki ürün sayısını getir")
    public ResponseEntity<ApiResponse<Long>> getCartItemCount() {
        Long userId = getCurrentUserId();
        Long count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count, "Sepet ürün sayısı getirildi"));
    }

    @PostMapping("/validate-stock")
    @Operation(summary = "Sepet stok kontrolü")
    public ResponseEntity<ApiResponse<Void>> validateCartStock() {
        Long userId = getCurrentUserId();
        cartService.validateCartStock(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Stok kontrolü başarılı"));
    }

    // Helper method - AuthService kullan
    private Long getCurrentUserId() {
        return authService.getCurrentUser().getId();
    }
}