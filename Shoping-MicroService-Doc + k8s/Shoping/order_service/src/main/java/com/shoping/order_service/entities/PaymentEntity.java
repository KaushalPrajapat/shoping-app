package com.shoping.order_service.entities;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentEntity {
    private long paymentId;
    private String paymentMode;
    private long orderId;
    private long userId;
    private double payAmount;
    
    private String paymentReferance; // One unique UUID
    private Instant paymentDate;
    private String paymentStatus;


}
