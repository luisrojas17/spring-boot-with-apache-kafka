package com.acme.product.create.controller.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private String title;
    private BigDecimal price;
    private Integer quantity;
}
