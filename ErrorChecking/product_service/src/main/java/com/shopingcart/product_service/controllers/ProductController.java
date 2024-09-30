package com.shopingcart.product_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopingcart.product_service.entities.Product;
import com.shopingcart.product_service.services.impl.ProductServiceImpl;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    // Get One product (based on id) from DB
    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "prodId") long prodId) {
        Product results = productService.getProductById(prodId);
        return ResponseEntity.ok(results);
    }

}
