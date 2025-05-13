package com.acme.product.create.controller;

import com.acme.product.create.controller.model.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.acme.common.constants.APIConstants.PRODUCTS_PATH;

@RequestMapping(PRODUCTS_PATH)
public interface CreateProductController {

    @PostMapping
    ResponseEntity<String> create(@RequestBody ProductDto productDto);
}
