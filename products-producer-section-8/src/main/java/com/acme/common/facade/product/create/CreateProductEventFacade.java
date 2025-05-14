package com.acme.common.facade.product.create;

import com.acme.common.domain.Product;

public interface CreateProductEventFacade {

    void publishEvent(Product product) throws Exception;
}
