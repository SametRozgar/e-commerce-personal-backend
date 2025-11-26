package com.example.e_commerce.service;

import com.example.e_commerce.entity.*;
import com.example.e_commerce.repository.OrderRepository;
import com.example.e_commerce.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final CartService cartService;
    private final ProductVariantService productVariantService;

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Page<Order> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
    }

    public Order getOrderWithItems(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
    }

    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public User getCurrentUser() {
        // Gerçek implementasyon - AuthService'ten alınabilir
        // Şimdilik basit bir implementasyon:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Kullanıcı giriş yapmamış");
        }
        String email = authentication.getName();
        return userService.findByEmail(email);
    }

    @Transactional
    public Order createOrderFromCart(Long userId, Long addressId) {
        User user = userService.findById(userId);
        Address address = addressService.getAddress(addressId);

        // Adresin kullanıcıya ait olduğunu kontrol et
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Adres kullanıcıya ait değil");
        }

        // Sepet stok kontrolü
        cartService.validateCartStock(userId);

        Cart cart = cartService.getCartWithItems(userId);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Sepet boş");
        }

        // Siparişi oluştur
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .address(address)
                .status(Order.OrderStatus.PENDING)
                .totalAmount(0.0)
                .build();

        Order savedOrder = orderRepository.save(order);

        Double totalAmount = 0.0;

        // Sipariş öğelerini oluştur ve stokları düşür
        for (CartItem cartItem : cart.getCartItems()) {
            ProductVariant variant = cartItem.getProductVariant();
            Double itemPrice = variant.getPrice() * variant.getPriceMultiplier();
            Double itemTotal = itemPrice * cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .productVariant(variant)
                    .quantity(cartItem.getQuantity())
                    .price(itemPrice)
                    .build();

            orderItemRepository.save(orderItem);
            totalAmount += itemTotal;

            // Stoku düşür
            productVariantService.decreaseStock(variant.getId(), cartItem.getQuantity());
        }

        savedOrder.setTotalAmount(totalAmount);
        Order finalOrder = orderRepository.save(savedOrder);

        // Sepeti temizle
        cartService.clearCart(userId);

        return finalOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrder(orderId);

        // Önceki durumu kaydet
        Order.OrderStatus previousStatus = order.getStatus();
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        // Durum değişikliği log'u (burada event publish edilebilir)
        logOrderStatusChange(orderId, previousStatus, status);

        return updatedOrder;
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = getOrderWithItems(orderId);

        // Siparişin kullanıcıya ait olduğunu kontrol et
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu siparişi iptal etme yetkiniz yok");
        }

        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.PREPARING) {
            throw new RuntimeException("Bu sipariş iptal edilemez");
        }

        // Stokları geri al
        for (OrderItem orderItem : order.getOrderItems()) {
            productVariantService.increaseStock(
                    orderItem.getProductVariant().getId(),
                    orderItem.getQuantity()
            );
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<Object[]> getOrderCountByStatus() {
        return orderRepository.findOrderCountByStatus();
    }

    public Long getTotalOrderCount() {
        return orderRepository.count();
    }

    public Double getTotalRevenue() {
        Double revenue = orderRepository.getTotalRevenueBetweenDates(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now()
        );
        return revenue != null ? revenue : 0.0;
    }

    public Double getRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = orderRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    public List<Object[]> getTopSellingProducts() {
        return orderItemRepository.findTopSellingProducts();
    }

    public List<Object[]> getSalesBySize() {
        return orderItemRepository.findSalesBySize();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void logOrderStatusChange(Long orderId, Order.OrderStatus from, Order.OrderStatus to) {
        // Burada order status değişikliği loglanabilir veya event publish edilebilir
        System.out.println("Sipariş " + orderId + " durumu değişti: " + from + " -> " + to);
    }
}