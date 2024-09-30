package com.shopingcart.product_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopingcart.product_service.entities.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Optional<Product> findByProdNameAndProdPrice(String prodName, double prodPrice);

    List<Product> findAllByProdName(String prodName);

    List<Product> findAllByProdPrice(double prodPrice);

}
