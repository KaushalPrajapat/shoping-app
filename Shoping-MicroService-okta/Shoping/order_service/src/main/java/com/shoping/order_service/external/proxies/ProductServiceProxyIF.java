package com.shoping.order_service.external.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.shoping.order_service.entities.ProductProxy;

@FeignClient(name = "PRODUCT-SERVICE/product")
@Service
public interface ProductServiceProxyIF{

    @GetMapping("/{prodId}")
    ResponseEntity<ProductProxy> getProductById(@PathVariable long prodId);
    
    @PutMapping("/out/{prodId}/{outQuantity}")
    public ResponseEntity<?> outUpdateProductQuantity(@PathVariable long prodId, @PathVariable long outQuantity);

    @PutMapping("/in/{prodId}/{outQuantity}")
    public ResponseEntity<?> inUpdateProductQuantity(@PathVariable long prodId, @PathVariable long outQuantity);
    
}
