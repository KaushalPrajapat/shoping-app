package com.shoping.payment_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.payment_service.entities.PaymentEntity;
import com.shoping.payment_service.services.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentContoller {
    @Autowired
    private PaymentService paymentService;
    @PostMapping("/do")
    public ResponseEntity<?> doPayment(@RequestBody PaymentEntity payment){
        return ResponseEntity.ok(paymentService.doPayment(payment));
    }
    @PutMapping("/undo/{paymentId}")
    public ResponseEntity<?> undoPayment(@PathVariable long paymentId){
        return ResponseEntity.ok(paymentService.undoPayment(paymentId));
    }
    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam double payAmount,
    @RequestParam long orderId,
    @RequestParam String paymentMode,
    @RequestParam long paymentId,
    @RequestParam String paymentReferance,
    @RequestParam long userId,
    @RequestParam String paymentStatus){

        return ResponseEntity.ok(paymentService.pay(payAmount,orderId,paymentMode,paymentId,paymentReferance,userId,paymentStatus));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPaymentsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(paymentService.getAllPaymentsByUserId(userId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentByPaymentId(@PathVariable long paymentId){
        return ResponseEntity.ok(paymentService.getPaymentByPaymentId(paymentId));
    }
}
    
    
    
    

