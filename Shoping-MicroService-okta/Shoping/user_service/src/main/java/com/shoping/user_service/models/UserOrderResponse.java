package com.shoping.user_service.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderResponse {
    // USER
    private long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    // ORDER
    // LIST OF ORDER - which include payments and product init
    List<OrderModel> orders;
    // PRODUCT
    List<PaymentEntity> payments;
    // PAYMENT
}
