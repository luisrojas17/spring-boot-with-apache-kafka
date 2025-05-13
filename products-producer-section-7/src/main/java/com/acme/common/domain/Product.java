package com.acme.common.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private String id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
