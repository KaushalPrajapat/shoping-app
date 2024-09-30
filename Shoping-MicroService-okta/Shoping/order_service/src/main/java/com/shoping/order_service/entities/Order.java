package com.shoping.order_service.entities;

import java.time.Instant;

import com.shoping.order_service.models.PaymentMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @Column(name = "product_id")
    private Long prodId;
    @Column(name = "quantity")
    private long orderQuantity;
    @Column(name = "total")
    private double totalAmount;
    @Column(name = "order_date_time")
    private Instant orderDate;

    private String orderStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;
    // It's one to one relation I thing It will not be needed
    private long paymentId;
    private long userId;

}