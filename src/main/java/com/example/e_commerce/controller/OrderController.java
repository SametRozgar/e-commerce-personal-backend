package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.order.OrderCreateRequest;
import com.example.e_commerce.dto.order.OrderStatusUpdateRequest;
import com.example.e_commerce.entity.Order;
import com.example.e_commerce.service.AuthService;
import com.example.e_commerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Sipariş yönetimi API'leri")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService; // AuthService eklendi

    @PostMapping
    @Operation(summary = "Yeni sipariş oluştur")
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Long userId = getCurrentUserId();
        Order order = orderService.createOrderFromCart(userId, request.getAddressId());
        return ResponseEntity.ok(ApiResponse.success(order, "Sipariş başarıyla oluşturuldu"));
    }

    @GetMapping
    @Operation(summary = "Kullanıcının siparişlerini getir")
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders() {
        Long userId = getCurrentUserId();
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Siparişler getirildi"));
    }

    @GetMapping("/page")
    @Operation(summary = "Sayfalı sipariş listesi")
    public ResponseEntity<ApiResponse<Page<Order>>> getUserOrdersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sayfalı siparişler getirildi"));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Sipariş detayını getir")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        Order order = orderService.getOrderWithItems(orderId);

        // Siparişin kullanıcıya ait olduğunu kontrol et
        if (!order.getUser().getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Bu siparişi görüntüleme yetkiniz yok"));
        }

        return ResponseEntity.ok(ApiResponse.success(order, "Sipariş detayı getirildi"));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Sipariş numarasına göre sipariş getir")
    public ResponseEntity<ApiResponse<Order>> getOrderByNumber(@PathVariable String orderNumber) {
        Long userId = getCurrentUserId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);

        if (!order.getUser().getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Bu siparişi görüntüleme yetkiniz yok"));
        }

        return ResponseEntity.ok(ApiResponse.success(order, "Sipariş getirildi"));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Siparişi iptal et")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        Order order = orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(order, "Sipariş iptal edildi"));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tüm siparişleri getir (ADMIN)")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders, "Tüm siparişler getirildi"));
    }

    @GetMapping("/admin/page")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sayfalı tüm siparişler (ADMIN)")
    public ResponseEntity<ApiResponse<Page<Order>>> getAllOrdersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(orders, "Sayfalı siparişler getirildi"));
    }

    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sipariş durumunu güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        Order order = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(order, "Sipariş durumu güncellendi"));
    }

    @GetMapping("/admin/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sipariş istatistikleri (ADMIN)")
    public ResponseEntity<ApiResponse<Long>> getOrderCount() {
        Long count = orderService.getTotalOrderCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Sipariş sayısı getirildi"));
    }

    @GetMapping("/admin/stats/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toplam gelir (ADMIN)")
    public ResponseEntity<ApiResponse<Double>> getTotalRevenue() {
        Double revenue = orderService.getTotalRevenue();
        return ResponseEntity.ok(ApiResponse.success(revenue, "Toplam gelir getirildi"));
    }

    @GetMapping("/admin/stats/top-products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "En çok satan ürünler (ADMIN)")
    public ResponseEntity<ApiResponse<List<Object[]>>> getTopSellingProducts() {
        List<Object[]> topProducts = orderService.getTopSellingProducts();
        return ResponseEntity.ok(ApiResponse.success(topProducts, "En çok satan ürünler getirildi"));
    }

    // Helper method - AuthService kullan
    private Long getCurrentUserId() {
        return authService.getCurrentUser().getId();
    }
}