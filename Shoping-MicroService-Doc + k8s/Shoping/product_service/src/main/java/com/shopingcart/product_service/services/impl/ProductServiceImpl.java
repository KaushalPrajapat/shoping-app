package com.shopingcart.product_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopingcart.product_service.entities.Product;
import com.shopingcart.product_service.errors.CustomException;
import com.shopingcart.product_service.models.ProductDTO;
import com.shopingcart.product_service.repositories.ProductRepo;
import com.shopingcart.product_service.services.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductDTO> results = new ArrayList<>();
        for (Product prod : products) {
            ProductDTO productDto = new ProductDTO();
            BeanUtils.copyProperties(prod, productDto);
            results.add(productDto);
        }
        return results;

    }

    @Override
    public Product addProduct(ProductDTO product) {
        product.setProdName(product.getProdName().toLowerCase());
        product.setProdDesc(product.getProdDesc().toLowerCase());
        Optional<Product> isExists = productRepo.findByProdNameAndProdPrice(product.getProdName(),
                product.getProdPrice());
        if (isExists.isPresent()) {
            isExists.get().setProdQuantity(isExists.get().getProdQuantity() + product.getProdQuantity());
            productRepo.save(isExists.get());
            return isExists.get();
        }
        Product saveProduct = new Product();
        BeanUtils.copyProperties(product, saveProduct);
        return productRepo.save(saveProduct);
    }

    @Override
    public ProductDTO getProductById(long prodId) {

        Optional<Product> product = null;
        try {
            product = productRepo.findById(prodId);
        } catch (Exception e) {
            throw new CustomException("Table are not ready", "DATABSE_ISSUE_PRODUCT");
        }
        if (product == null || product.isEmpty()) {
            throw new CustomException("Product with given id " + prodId + " doesn't exists.", "PRODUCT_NOT_FOUND");
        }
        ProductDTO productDto = new ProductDTO();

        BeanUtils.copyProperties(product.get(), productDto);
        return productDto;
    }

    @Override
    public List<ProductDTO> getAllProductsByProdnameAndOrProdPrice(String prodName, String productPrice) {
        double prodPrice = 0;
        if (!productPrice.equals("empty")) {
            try {
                prodPrice = Double.parseDouble(productPrice);
            } catch (Exception e) {
                prodPrice = 0;
                log.info("Product Price Must be Numeric 😒 ");
            }
        }
        prodName = prodName.toLowerCase();
        if (prodName.equals("empty") && prodPrice == 0) {
            return getAllProducts();
        } else if (!prodName.equals("empty") && prodPrice == 0) {
            return getProductsByProdName(prodName);
        } else if (prodName.equals("empty") && prodPrice != 0) {
            return getProductsByProdPrice(prodPrice);
        } else {
            return getProductsByProdNameAndProdPrice(prodName, prodPrice);
        }
    }

    private List<ProductDTO> getProductsByProdName(String prodName) {
        List<Product> products = productRepo.findAll();
        List<Product> containsName = new ArrayList<>();

        containsName = products.stream().filter(prod -> prod.getProdName().contains(prodName)).toList();

        List<ProductDTO> results = new ArrayList<>();
        for (Product prod : containsName) {
            ProductDTO productDto = new ProductDTO();
            BeanUtils.copyProperties(prod, productDto);
            results.add(productDto);
        }
        return results;
    }

    private List<ProductDTO> getProductsByProdPrice(double prodPrice) {
        List<Product> products = productRepo.findAllByProdPrice(prodPrice);
        List<ProductDTO> results = new ArrayList<>();
        for (Product prod : products) {
            ProductDTO productDto = new ProductDTO();
            BeanUtils.copyProperties(prod, productDto);
            results.add(productDto);
        }
        return results;
    }

    private List<ProductDTO> getProductsByProdNameAndProdPrice(String prodName, double prodPrice) {
        List<ProductDTO> products = getProductsByProdName(prodName);
        List<ProductDTO> results = new ArrayList<>();

        results = products.stream().filter(prod -> prod.getProdPrice() == prodPrice).toList();
        return results;
    }

    @Override
    public Product deleteProductByProdId(long prodId) {
        Product product = getProductByIdReturnProduct(prodId);
        productRepo.delete(product);
        return product;

    }

    @Override
    public ProductDTO updateProduct(long prodId, Product product) {
        Optional<Product> isExists = productRepo.findById(prodId);
        if (isExists.isPresent()) {
            if (!isExists.get().getProdName().equals(product.getProdName().toLowerCase())) {
                isExists.get().setProdName(product.getProdName().toLowerCase());
            }
            if (!isExists.get().getProdDesc().equals(product.getProdDesc().toLowerCase())) {
                isExists.get().setProdDesc(product.getProdDesc().toLowerCase());
            }
            if (isExists.get().getProdPrice() != product.getProdPrice()) {
                isExists.get().setProdPrice(product.getProdPrice());
            }
            if (isExists.get().getProdQuantity() != product.getProdQuantity() && product.getProdQuantity() > 0) {
                isExists.get().setProdQuantity(product.getProdQuantity() + isExists.get().getProdQuantity());
            }

            productRepo.save(isExists.get());
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(isExists.get(), productDTO);
            return productDTO;
        }
        throw new CustomException("Product with given id " + prodId + " doesn't exists.", "PRODUCT_NOT_FOUND");
    }

    @Override
    public boolean outUpdateProduct(long prodId, long outQuantity) {
        Optional<Product> isExists = productRepo.findById(prodId);
        log.info(outQuantity + " " + isExists.get().getProdQuantity());
        if (outQuantity > isExists.get().getProdQuantity()) {
            throw new CustomException("Quantity is less than you want order" + isExists.get().getProdQuantity(),
                    "INSUFFIENT_QUENTITY");
        }
        isExists.get().setProdQuantity(isExists.get().getProdQuantity() - outQuantity);
        productRepo.save(isExists.get());
        return true;
    }

    @Override
    public boolean inUpdateProduct(long prodId, long inQuantity) {
        Optional<Product> isExists = productRepo.findById(prodId);
        isExists.get().setProdQuantity(isExists.get().getProdQuantity() + inQuantity);
        productRepo.save(isExists.get());
        return true;
    }

    private Product getProductByIdReturnProduct(long prodId) {

        Optional<Product> product = null;
        try {
            product = productRepo.findById(prodId);
        } catch (Exception e) {
            throw new CustomException("Table are not ready", "DATABSE_ISSUE");
        }
        if (product == null || product.isEmpty()) {
            throw new CustomException("Product with given id " + prodId + " doesn't exists.", "PRODUCT_NOT_FOUND");
        }
        return product.get();
    }

}
