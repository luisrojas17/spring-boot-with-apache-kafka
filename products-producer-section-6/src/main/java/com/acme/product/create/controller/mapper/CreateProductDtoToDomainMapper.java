package com.acme.product.create.controller.mapper;

import com.acme.common.domain.Product;
import com.acme.product.create.controller.model.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateProductDtoToDomainMapper {

    Product apply(ProductDto productDto);
}
