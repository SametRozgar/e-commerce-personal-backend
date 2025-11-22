package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.productVariant pv JOIN FETCH pv.product WHERE ci.cart.id = :cartId")
    List<CartItem> findByCartIdWithVariants(@Param("cartId") Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.productVariant.id = :variantId")
    void deleteByCartIdAndVariantId(@Param("cartId") Long cartId, @Param("variantId") Long variantId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.id = :cartItemId AND ci.cart.id = :cartId")
    int updateQuantity(@Param("cartId") Long cartId, @Param("cartItemId") Long cartItemId, @Param("quantity") Integer quantity);

    boolean existsByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart.user.id = :userId")
    Long getTotalItemCountByUserId(@Param("userId") Long userId);
}