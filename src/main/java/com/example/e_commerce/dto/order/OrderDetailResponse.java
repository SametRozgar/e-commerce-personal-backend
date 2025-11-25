package com.example.e_commerce.dto.order;

import com.example.e_commerce.dto.address.AddressResponse;
import com.example.e_commerce.dto.user.UserResponse;
import com.example.e_commerce.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailResponse {
    private Long id;
    private String orderNumber;
    private UserResponse user;
    private AddressResponse address;
    private List<OrderItemResponse> orderItems;
    private Order.OrderStatus status;
    private Double totalAmount;
    private String paymentId;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
