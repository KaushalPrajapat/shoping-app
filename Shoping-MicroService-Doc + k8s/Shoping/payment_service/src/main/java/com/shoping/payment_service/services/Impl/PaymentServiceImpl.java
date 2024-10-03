package com.shoping.payment_service.services.Impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.payment_service.entities.PaymentEntity;
import com.shoping.payment_service.entities.Users;
import com.shoping.payment_service.errors.CustomException;
import com.shoping.payment_service.external.proxies.UserServiceProxyIF;
import com.shoping.payment_service.repositories.PaymentEntityRepo;
import com.shoping.payment_service.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserServiceProxyIF proxy;
    @Autowired
    private PaymentEntityRepo paymentRepo;

    @SuppressWarnings("null")
    @Override
    public PaymentEntity doPayment(PaymentEntity payment) {
        // Check balance If ok then- call /balance/do/userId/balance
        ResponseEntity<?> userResponseEntity = proxy.getUsersByUserId(payment.getUserId());
        Users user = null;
        try {
            user = (Users) userResponseEntity.getBody();
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getHttpStatus());
        }

        // if (user.getUserId() == 0) {
        //     throw new CustomException("User Doesn't exists with id - " + payment.getUserId(), "NO_USER");
        // }

        if (user.getBalance() < payment.getPayAmount()) {
            throw new CustomException("Not enough money ", "INSUFFICENT_BALANCE_PAYMENT");
        }
        proxy.doPayment(payment.getUserId(), payment.getPayAmount());
        payment.setPaymentDate(Instant.now());
        return paymentRepo.save(payment);
    }

    @SuppressWarnings("null")
    @Override
    public PaymentEntity undoPayment(long paymentId) {
        Optional<PaymentEntity> optionalPayment = paymentRepo.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new CustomException("Payment Id doesn't exists", "NO_PAYMENT_FOUND");
        }
        PaymentEntity payment = optionalPayment.get();
        if (payment.getPaymentStatus().equals("CANCELLED")) {
            throw new CustomException("Payment Already Closed, Cashback done", "CANCELLED_PAYMENT");
        }
        ResponseEntity<?> userResponseEntity = proxy.getUsersByUserId(payment.getUserId());
        Users user = null;
        try {
            user = (Users) userResponseEntity.getBody();
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(), ex.getHttpStatus());
        }
        // System.out.println(user.getUserId());
        // if (user.getUserId() == 0) {
        //     throw new CustomException("User Doesn't exists with id - " + payment.getUserId(), "NO_USER");
        // }
        proxy.undoPayment(user.getUserId(), payment.getPayAmount());
        payment.setPaymentDate(Instant.now());
        payment.setPaymentStatus("CANCELLED");
        return paymentRepo.save(payment);
    }

    @SuppressWarnings("null")
    @Override
    public PaymentEntity pay(double payAmount, long orderId, String paymentMode, long paymentId,
            String paymentReferance, long userId, String paymentStatus) {
        // Prepare payment object
        PaymentEntity payment = new PaymentEntity();
        //
        System.out.println("After security implementation");
        payment.setOrderId(orderId);
        payment.setPayAmount(payAmount);
        payment.setPaymentDate(Instant.now());
        payment.setPaymentMode(paymentMode);
        payment.setPaymentReferance(paymentReferance);
        payment.setPaymentStatus(paymentStatus);
        payment.setUserId(userId);
        // Check balance If ok then- call /balance/do/userId/balance
        ResponseEntity<?> userResponseEntity = proxy.getUsersByUserId(payment.getUserId());
        Users user = null;
        try {
            user = (Users) userResponseEntity.getBody();
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage() , ex.getHttpStatus());
        }
        // System.out.println(user == null);
        // if (user.getUserId() == 0) {
        //     throw new CustomException("User Doesn't exists with id - " + payment.getUserId(), "NO_USER_PAYMENT");
        // }
        if (user.getBalance() < payment.getPayAmount()) {
            throw new CustomException("Not enough money 2", "INSUFFICENT_BALANCE_PAYMENT");
        }
        proxy.doPayment(payment.getUserId(), payment.getPayAmount());
        payment.setPaymentDate(Instant.now());
        return paymentRepo.save(payment);

    }

    @Override
    public List<PaymentEntity> getAllPaymentsByUserId(long userId) {
        Optional<List<PaymentEntity>> allByUserId = paymentRepo.findAllByUserId(userId);
        return allByUserId.get();
    }

    @Override
    public PaymentEntity getPaymentByPaymentId(long paymentId) {
        Optional<PaymentEntity> paymentById = null;
        try {
            paymentById = paymentRepo.findById(paymentId);
        } catch (Exception e) {
        }
        if (paymentById == null) {
            throw new CustomException("Tables are not ready", "DATABASE_ISSUE_PAYMNET");
        }
        return paymentById
                .orElse(new PaymentEntity(0, "UNKNOWN", "UNKNOWN", 0, 0, Instant.now(), "UNKNOWN", 0));
    }
}