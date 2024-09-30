package com.shoping.order_service.services;

import java.util.List;
import java.util.Optional;

import com.shoping.order_service.entities.Order;
import com.shoping.order_service.models.OrderRequest;
import com.shoping.order_service.models.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest or);

    OrderResponse deleteOrder(long orderId);

    OrderResponse getOrderById(long orderId);

    List<Optional<Order>> getAllOrderByUserId(long userId);


    
} 