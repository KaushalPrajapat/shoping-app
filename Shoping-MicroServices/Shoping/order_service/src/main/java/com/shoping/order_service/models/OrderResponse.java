package com.shoping.order_service.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long orderQuantity;
    // From Product Service
    private String prodName;
    private double prodPrice;
    // FromPayment
    private long paymentId;
    private String paymentReferance;
    private double totalAmount;
    private PaymentMode paymentMode;

    // From User
    private String userName;
    private String userEmail;

    // Extra if somthing need to be shown
    private String message;
}
