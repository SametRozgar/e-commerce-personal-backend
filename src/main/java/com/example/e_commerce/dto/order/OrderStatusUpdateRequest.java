package com.example.e_commerce.dto.order;

import com.example.e_commerce.entity.Order;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private Order.OrderStatus status;
}
