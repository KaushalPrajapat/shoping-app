package com.shoping.user_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private long orderId;
    private long orderQuantity;
    private String orderDate;
    private String orderStatus;
    // Payments -- ye isi ke pass h
    // private double totalAmount;
    // private String paymentMode;
    // private long paymentId;
    PaymentEntity payment;
    // Product
    ProductModel products;
}