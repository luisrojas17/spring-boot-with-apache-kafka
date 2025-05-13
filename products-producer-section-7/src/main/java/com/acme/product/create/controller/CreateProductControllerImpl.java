package com.acme.product.create.controller;

import com.acme.product.create.controller.mapper.CreateProductDtoToDomainMapper;
import com.acme.product.create.controller.model.ProductDto;
import com.acme.product.create.usecase.CreateProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateProductControllerImpl implements CreateProductController {

    private final CreateProductDtoToDomainMapper createProductDtoToDomainMapper;

    private final CreateProductUseCase createProductUseCase;

    @Override
    public ResponseEntity<String> create(ProductDto productDto) {

        log.info("Creating product dto [{}].", productDto);

        String productId = createProductUseCase.create(
                createProductDtoToDomainMapper.apply(productDto));

        log.info("Product created with id [{}].", productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
