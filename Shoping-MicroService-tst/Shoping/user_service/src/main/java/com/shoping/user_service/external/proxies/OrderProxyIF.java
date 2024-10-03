package com.shoping.user_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoping.user_service.errors.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "ORDER-SERVICE/order")
public interface OrderProxyIF {
    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getAllOrderByUserId(@PathVariable long userId);

    // Updated fallback method with matching signature
    default ResponseEntity<?> fallback(long prodId, Throwable throwable) {
        throw new CustomException("Order is down", "ORDER_SERVICE_DOWN");
    }
    default ResponseEntity<?> fallback(long prodId, java.lang.Throwable t1, java.lang.Throwable t2) {
        throw new CustomException("Order Service is down t1, t2", "ORDER_SERVICE_DOWN");
    }
}
