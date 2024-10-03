package com.shoping.user_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoping.user_service.errors.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentProxyIF {
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPaymentsByUserId(@PathVariable long userId);

    @GetMapping("{paymentId}")
    public ResponseEntity<?> getPaymentByPaymentId(@PathVariable long paymentId);

    // Updated fallback method with matching signature
    default ResponseEntity<?> fallback(long prodId, Throwable throwable) {
        throw new CustomException("PAYMENT is down", "PAYMENT_SERVICE_DOWN");
    }
    default ResponseEntity<?> fallback(long prodId, java.lang.Throwable t1, java.lang.Throwable t2) {
        throw new CustomException("PAYMENT is down 2", "PAYMENT_SERVICE_DOWN");
    }
}
