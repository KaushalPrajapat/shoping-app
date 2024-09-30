package com.shopingcart.product_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.shopingcart.product_service.entities.Product;
import com.shopingcart.product_service.models.ProductDTO;
import com.shopingcart.product_service.services.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Fetch All product from DB
    @GetMapping()
    public ResponseEntity<?> getAllProduct() {
        List<ProductDTO> results = productService.getAllProducts();
        return ResponseEntity.ok(results);
    }

    // Get One product (based on id) from DB
    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "prodId") long prodId) {
        ProductDTO results = productService.getProductById(prodId);
        return ResponseEntity.ok(results);
    }

    // Getting products List based on Name or Price or both (It's fuzzy search for
    // name means contains one)
    @GetMapping("/filter")
    public ResponseEntity<?> getAllProductByProdNameAndOrProdPrice(
            @RequestParam(name = "prodName", required = false, defaultValue = "empty") String prodName,
            @RequestParam(required = false, defaultValue = "empty") String prodPrice) {
                System.out.println(prodName + " price " + prodPrice);
        List<ProductDTO> results = productService.getAllProductsByProdnameAndOrProdPrice(prodName, prodPrice);
        return ResponseEntity.ok(results);

    }

    // Save Product to DB
    @PostMapping()
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO product) {
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Delete Product from DB
    @DeleteMapping("/{prodId}")
    public ResponseEntity<Product> deleteProductByProdId(@PathVariable long prodId){
        Product deletedProducted = productService.deleteProductByProdId(prodId);
        return ResponseEntity.ok(deletedProducted);
    }

    // Update any products You need product Id to do so...
    @PutMapping("/{prodId}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product , @PathVariable long prodId) {
        ProductDTO productUpdated = productService.updateProduct(prodId,product);
        return ResponseEntity.ok(productUpdated);
    }

    @PutMapping("/out/{prodId}/{outQuantity}")
    public ResponseEntity<?> outUpdateProductQuantity(@PathVariable long prodId, @PathVariable long outQuantity){
        return ResponseEntity.ok(productService.outUpdateProduct(prodId, outQuantity));
    }

    @PutMapping("/in/{prodId}/{inQuantity}")
    public ResponseEntity<?> inUpdateProductQuantity(@PathVariable long prodId, @PathVariable long inQuantity){
        return ResponseEntity.ok(productService.inUpdateProduct(prodId, inQuantity));
    }
    
}
