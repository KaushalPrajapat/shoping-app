package com.shoping.user_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.user_service.entities.Users;
import com.shoping.user_service.services.UsersService;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        return ResponseEntity.ok(usersService.addUser(user));
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsersByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.getUserByUserId(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Users> deleteUsersByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.deleteUserByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Users> deleteUsersByUserId(@RequestBody Users user, @PathVariable long userId) {
        return ResponseEntity.ok(usersService.updateUser(user, userId));
    }

    @PutMapping("/addbalance/{userId}")
    public ResponseEntity<Users> addBalance(@PathVariable long userId, @RequestParam double balance) {
        return ResponseEntity.ok(usersService.addBalance(userId, balance));
    }

    @PutMapping("/balance/do/{userId}/{balance}")
    public ResponseEntity<Users> doPayment(@PathVariable long userId, @PathVariable double balance) {
        return ResponseEntity.ok(usersService.doPayment(userId, balance));
    }

    @PutMapping("/balance/undo/{userId}/{balance}")
    public ResponseEntity<Users> undoPayment(@PathVariable long userId, @PathVariable double balance) {
        return ResponseEntity.ok(usersService.undoPayment(userId, balance));
    }

    @GetMapping("/getbalance/{userId}")
    public ResponseEntity<Double> getBalance(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.getBalance(userId));
    }

    // Get All Orders of a user - by user-Id
    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getAllOrdersOfAUser(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getAllOrdersOfAUser(userId));
    }
    // Get All Payments done by a user - by user-Id
    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getAllPaymentsOfAUser(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getAllPaymentsOfAUser(userId));
    }

}