package com.shoping.payment_service.services;

import java.util.List;

import com.shoping.payment_service.entities.PaymentEntity;
public interface PaymentService {

    PaymentEntity doPayment(PaymentEntity payment);

    PaymentEntity undoPayment(long paymentId);

    PaymentEntity pay(double payAmount, long orderId, String paymentMode, long paymentId, String paymentReferance, long userId,
            String paymentStatus);

    List<PaymentEntity> getAllPaymentsByUserId(long userId);

    PaymentEntity getPaymentByPaymentId(long paymentId);

    
} 