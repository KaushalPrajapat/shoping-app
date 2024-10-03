package com.shoping.order_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoping.order_service.entities.Users;
import com.shoping.order_service.errors.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "USER-SERVICE/user")
public interface UserServiceProxyIF {
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUsersByUserId(@PathVariable long userId);

    default ResponseEntity<Users> fallback(Exception e) {
        throw new CustomException("User Service is not available",
                "UNAVAILABLE");
    }
}
