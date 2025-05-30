package com.acme.product.create.usecase;

import com.acme.common.domain.Product;
import com.acme.common.facade.product.create.CreateProductEventFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final CreateProductEventFacade createProductEventFacade;

    @Override
    public String create(Product product) {

        log.info("Creating product {}", product);

        String productId = null;

        try {

            // TODO: Persist Product Details into database table before publishing an Event
            // TODO:The product id should be generated by the database
            productId = UUID.randomUUID().toString();
            product.setId(productId);

            // TODO: If there was any exception, the product should not be persisted make a Rollback
            createProductEventFacade.publishEvent(product);

        } catch (Exception e) {
            log.error("Error while publishing product created event.", e);

            throw new RuntimeException(e);

        }

        return productId;

    }
}
