package com.acme.common.events;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductCreatedEvent {

    private String id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
