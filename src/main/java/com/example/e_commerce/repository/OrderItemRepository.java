package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Order;
import com.example.e_commerce.entity.OrderItem;
import com.example.e_commerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByProductVariant(ProductVariant productVariant);

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.productVariant pv JOIN FETCH pv.product WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithVariants(@Param("orderId") Long orderId);

    @Query("SELECT pv.product.name, SUM(oi.quantity) FROM OrderItem oi JOIN oi.productVariant pv GROUP BY pv.product.id, pv.product.name ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProducts();

    @Query("SELECT pv.size, SUM(oi.quantity) FROM OrderItem oi JOIN oi.productVariant pv GROUP BY pv.size")
    List<Object[]> findSalesBySize();

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi JOIN oi.order o WHERE o.status = 'DELIVERED' AND oi.productVariant.product.id = :productId")
    Long getTotalSoldQuantityByProductId(@Param("productId") Long productId);
}