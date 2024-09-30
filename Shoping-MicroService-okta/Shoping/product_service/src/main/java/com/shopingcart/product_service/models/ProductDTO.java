package com.shopingcart.product_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {
    private String prodName;
    private String prodDesc;
    private double prodPrice;
    private long prodQuantity;
}
