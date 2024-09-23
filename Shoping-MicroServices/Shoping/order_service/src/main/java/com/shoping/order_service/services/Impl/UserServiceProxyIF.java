package com.shoping.order_service.services.Impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoping.order_service.entities.Users;

@FeignClient(name = "USER-SERVICE/user")
public interface UserServiceProxyIF {
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUsersByUserId(@PathVariable long userId);
}
