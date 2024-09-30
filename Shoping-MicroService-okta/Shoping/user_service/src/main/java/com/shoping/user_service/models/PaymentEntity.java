package com.shoping.user_service.models;

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
    private double payAmount;
    private String paymentReferance; // One unique UUID
    private String paymentDate;


}
