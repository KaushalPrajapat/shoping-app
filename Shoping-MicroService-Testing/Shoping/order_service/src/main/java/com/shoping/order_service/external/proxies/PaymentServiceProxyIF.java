package com.shoping.order_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.shoping.order_service.entities.PaymentEntity;
import com.shoping.order_service.errors.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
@Service
public interface PaymentServiceProxyIF {
    @PostMapping( consumes = "application/x-www-form-urlencoded", name = "/do")
    public ResponseEntity<?> doPayment(@SpringQueryMap PaymentEntity payment);

    @PutMapping("/undo/{paymentId}")
    public ResponseEntity<?> undoPayment(@PathVariable long paymentId);

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@SpringQueryMap PaymentEntity payment);

    default ResponseEntity<?> fallback(Exception e) {
        throw new CustomException("Payment Service is not available",
                "UNAVAILABLE");
    }
}
