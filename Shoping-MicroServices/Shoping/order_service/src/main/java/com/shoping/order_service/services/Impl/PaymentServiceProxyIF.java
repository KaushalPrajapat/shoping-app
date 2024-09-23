package com.shoping.order_service.services.Impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.shoping.order_service.entities.PaymentEntity;

@FeignClient(name = "PAYMENT-SERVICE/payment")
@Service
public interface PaymentServiceProxyIF {
    @PostMapping( consumes = "application/x-www-form-urlencoded", name = "/do")
    public ResponseEntity<?> doPayment(@SpringQueryMap PaymentEntity payment);

    @PutMapping("/undo/{paymentId}")
    public ResponseEntity<?> undoPayment(@PathVariable long paymentId);

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@SpringQueryMap PaymentEntity payment);
}
