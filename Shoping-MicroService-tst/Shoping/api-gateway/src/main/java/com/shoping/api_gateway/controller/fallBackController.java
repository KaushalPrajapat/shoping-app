package com.shoping.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class fallBackController {
    
    @GetMapping("/paymentFallBack")
    public String paymentFallBack(){
        return "Payment service is down";
    }
    @GetMapping("/orderFallBack")
    public String orderFallBack(){
        return "Order service is down";
    }
    @GetMapping("/productFallBack")
    public String productFallBack(){
        return "PRODUCT service is down";
    }
    @GetMapping("/userFallBack")
    public String userFallBack(){
        return "USER service is down";
    }
}
