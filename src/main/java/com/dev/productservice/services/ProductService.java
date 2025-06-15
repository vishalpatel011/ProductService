package com.dev.productservice.services;

import com.dev.productservice.exceptions.ProductNotFoundException;
import com.dev.productservice.models.Product;

public interface ProductService {
    Product getProductById(long id) throws ProductNotFoundException;
}
