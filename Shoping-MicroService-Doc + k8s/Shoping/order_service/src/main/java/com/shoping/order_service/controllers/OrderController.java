package com.shoping.order_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.order_service.models.OrderRequest;
import com.shoping.order_service.models.OrderResponse;
import com.shoping.order_service.services.OrderService;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/place-order")
    @PreAuthorize("hasAuthority('Customer') || hasAuthority('Admin') || hasAuthority('SCOPE_internal')")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest){
        System.out.println("Placing order");
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        
        System.out.println("?YET TO RETURN");
        return ResponseEntity.ok(orderResponse);
    }

    @PreAuthorize("hasAuthority('Customer')")
    @PutMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable long orderId){
        OrderResponse orderResponse = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @PreAuthorize("hasAuthority('Customer') || hasAuthority('Admin') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PreAuthorize("hasAuthority('Customer') || hasAuthority('Admin') || hasAuthority('SCOPE_internal')")
    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getAllOrderByUserId(@PathVariable long userId){
        return ResponseEntity.ok(orderService.getAllOrderByUserId(userId));
    }

}
