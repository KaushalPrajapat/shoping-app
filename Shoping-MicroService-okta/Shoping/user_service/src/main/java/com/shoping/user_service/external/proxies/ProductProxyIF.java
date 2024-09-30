package com.shoping.user_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoping.user_service.errors.CustomError;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductProxyIF {
    @GetMapping("/{prodId}")
    public ResponseEntity<Object> getProductById(@PathVariable(name = "prodId") long prodId);

    // Updated fallback method with matching signature
    default ResponseEntity<Object> fallback(long prodId, Throwable throwable) {
        throw new CustomError("Product is down", "PRODUCT_SERVICE_DOWN");
    }
    default ResponseEntity<Object> fallback(long prodId, java.lang.Throwable t1, java.lang.Throwable t2) {
        throw new CustomError("Product Service is down t1, t2", "PRODUCT_SERVICE_DOWN");
    }
}
