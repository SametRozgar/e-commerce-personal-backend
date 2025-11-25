package com.example.e_commerce.dto.order;

import com.example.e_commerce.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchRequest {
    private String orderNumber;
    private Long userId;
    private Order.OrderStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
