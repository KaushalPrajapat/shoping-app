package com.shoping.payment_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.shoping.payment_service.entities.Users;

@FeignClient(name = "USER-SERVICE/user")
@Service
public interface UserServiceProxyIF {
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUsersByUserId(@PathVariable long userId);

    @PutMapping("/balance/do/{userId}/{balance}")
    ResponseEntity<Users> doPayment(@PathVariable long userId, @PathVariable double balance);

    @PutMapping("/balance/undo/{userId}/{balance}")
    ResponseEntity<Users> undoPayment(@PathVariable long userId, @PathVariable double balance );

    @GetMapping("/getbalance/{userId}")
    ResponseEntity<Double> getBalance(@PathVariable long userId);
}
