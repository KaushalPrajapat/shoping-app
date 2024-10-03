package com.shoping.order_service.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProxy {
    private long prodId;
    private String prodName;
    private String prodDesc;
    private long prodQuantity;
    private double prodPrice;
}
