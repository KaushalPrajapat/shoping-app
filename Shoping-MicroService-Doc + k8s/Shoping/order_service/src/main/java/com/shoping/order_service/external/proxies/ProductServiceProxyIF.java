package com.shoping.order_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.shoping.order_service.entities.ProductProxy;
import com.shoping.order_service.errors.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/product")
@Service
public interface ProductServiceProxyIF {

    @GetMapping("/{prodId}")
    ResponseEntity<ProductProxy> getProductById(@PathVariable long prodId);

    @PutMapping("/out/{prodId}/{outQuantity}")
    public ResponseEntity<?> outUpdateProductQuantity(@PathVariable long prodId, @PathVariable long outQuantity);

    @PutMapping("/in/{prodId}/{outQuantity}")
    public ResponseEntity<?> inUpdateProductQuantity(@PathVariable long prodId, @PathVariable long outQuantity);

    default ResponseEntity<?> fallback(Exception e) {
        throw new CustomException("Product Service is not available",
                "UNAVAILABLE");
    }
}
