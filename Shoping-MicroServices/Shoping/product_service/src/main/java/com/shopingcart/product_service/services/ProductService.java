package com.shopingcart.product_service.services;

import java.util.List;

import com.shopingcart.product_service.entities.Product;
import com.shopingcart.product_service.models.ProductDTO;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    Product addProduct(ProductDTO product);

    ProductDTO getProductById(long prodId);

    List<ProductDTO> getAllProductsByProdnameAndOrProdPrice(String prodName, String prodPrice);

    Product deleteProductByProdId(long prodId);

    ProductDTO updateProduct(long prodId, Product product);


    boolean outUpdateProduct(long prodId, long outQuantity);

    boolean inUpdateProduct(long prodId, long inQuantity);
}
