package com.shopingcart.product_service.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopingcart.product_service.entities.Product;
import com.shopingcart.product_service.errors.CustomException;
import com.shopingcart.product_service.repositories.ProductRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl{

    @Autowired
    private ProductRepo productRepo;

    public Product getProductById(long prodId) {
        Optional<Product> product = productRepo.findById(prodId);
        if (product.isPresent()) {
            
            return product.get();
        } else {
            throw new CustomException("Product with given id " + prodId + " doesn't exists.", "PRODUCT1_NOT_FOUND");
        }
    }
}
