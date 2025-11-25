package com.example.e_commerce.service;

import com.example.e_commerce.dto.cart.CartItemAddRequest;
import com.example.e_commerce.dto.cart.CartItemUpdateRequest;
import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.CartItem;
import com.example.e_commerce.entity.ProductVariant;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.CartItemRepository;
import com.example.e_commerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductVariantService productVariantService;

    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));
    }

    public Cart getCartWithItems(Long userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));
    }

    @Transactional
    public Cart createCartForUser(User user) {
        if (cartRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Kullanıcının zaten bir sepeti var");
        }

        Cart cart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }

    @Transactional
    public CartItem addItemToCart(Long userId, CartItemAddRequest request) {
        Cart cart = getCartWithItems(userId);
        ProductVariant variant = productVariantService.getVariant(request.getVariantId());

        // Stok kontrolü
        if (!productVariantService.isVariantAvailable(request.getVariantId(), request.getQuantity())) {
            throw new RuntimeException("Yetersiz stok");
        }

        // Sepette zaten bu varyant var mı?
        CartItem existingItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), request.getVariantId())
                .orElse(null);

        if (existingItem != null) {
            // Varsa miktarı güncelle
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (!productVariantService.isVariantAvailable(request.getVariantId(), newQuantity)) {
                throw new RuntimeException("Yetersiz stok");
            }
            existingItem.setQuantity(newQuantity);
            return cartItemRepository.save(existingItem);
        } else {
            // Yoksa yeni oluştur
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(request.getQuantity())
                    .build();
            return cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, CartItemUpdateRequest request) {
        Cart cart = getCartWithItems(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı"));

        // Sepet öğesinin kullanıcının sepetine ait olduğunu kontrol et
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bu öğe kullanıcının sepetinde değil");
        }

        // Stok kontrolü
        if (!productVariantService.isVariantAvailable(
                cartItem.getProductVariant().getId(), request.getQuantity())) {
            throw new RuntimeException("Yetersiz stok");
        }

        cartItem.setQuantity(request.getQuantity());
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartWithItems(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bu öğe kullanıcının sepetinde değil");
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartWithItems(userId);
        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    public Long getCartItemCount(Long userId) {
        return cartRepository.countCartItemsByUserId(userId);
    }

    public Double calculateCartTotal(Long userId) {
        Cart cart = getCartWithItems(userId);
        return cart.getTotalPrice();
    }

    @Transactional
    public void validateCartStock(Long userId) {
        Cart cart = getCartWithItems(userId);

        for (CartItem item : cart.getCartItems()) {
            if (!productVariantService.isVariantAvailable(
                    item.getProductVariant().getId(), item.getQuantity())) {
                throw new RuntimeException(
                        "Ürün stoku yetersiz: " +
                                item.getProductVariant().getProduct().getName() +
                                " - " + item.getProductVariant().getSize()
                );
            }
        }
    }
}
