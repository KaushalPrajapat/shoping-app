package com.shoping.order_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private long prodId;
    private long orderQuantity;
    private PaymentMode paymentMode;
    private long userId;
}
