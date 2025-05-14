package com.acme.common.facade.product.create.mapper;

import com.acme.common.domain.Product;
import com.acme.common.events.ProductCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateProductDomainToEventMapper {

    ProductCreatedEvent apply(Product product);
}
