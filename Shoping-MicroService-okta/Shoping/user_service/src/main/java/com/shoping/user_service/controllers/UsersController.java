package com.shoping.user_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("*")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        return ResponseEntity.ok(usersService.addUser(user));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.getUserByUserId(userId));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Users> deleteUsersByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.deleteUserByUserId(userId));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/{userId}")
    public ResponseEntity<Users> deleteUsersByUserId(@RequestBody Users user, @PathVariable long userId) {
        return ResponseEntity.ok(usersService.updateUser(user, userId));
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @PutMapping("/addbalance/{userId}")
    public ResponseEntity<Users> addBalance(@PathVariable long userId, @RequestParam double balance) {
        return ResponseEntity.ok(usersService.addBalance(userId, balance));
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @PutMapping("/balance/do/{userId}/{balance}")
    public ResponseEntity<Users> doPayment(@PathVariable long userId, @PathVariable double balance) {
        return ResponseEntity.ok(usersService.doPayment(userId, balance));
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @PutMapping("/balance/undo/{userId}/{balance}")
    public ResponseEntity<Users> undoPayment(@PathVariable long userId, @PathVariable double balance) {
        return ResponseEntity.ok(usersService.undoPayment(userId, balance));
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/getbalance/{userId}")
    public ResponseEntity<Double> getBalance(@PathVariable long userId) {
        return ResponseEntity.ok(usersService.getBalance(userId));
    }

    // Get All Orders of a user - by user-Id
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getAllOrdersOfAUser(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getAllOrdersOfAUser(userId));
    }
    // Get All Payments done by a user - by user-Id
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getAllPaymentsOfAUser(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getAllPaymentsOfAUser(userId));
    }

}