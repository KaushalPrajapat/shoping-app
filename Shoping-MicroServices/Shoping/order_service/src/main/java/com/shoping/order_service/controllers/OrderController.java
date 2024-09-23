package com.shoping.order_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.order_service.models.OrderRequest;
import com.shoping.order_service.models.OrderResponse;
import com.shoping.order_service.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable long orderId){
        OrderResponse orderResponse = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getAllOrderByUserId(@PathVariable long userId){
        return ResponseEntity.ok(orderService.getAllOrderByUserId(userId));
    }

}
