package com.dev.productservice.services;

import com.dev.productservice.dtos.CreateFakeStoreProductRequestDto;
import com.dev.productservice.exceptions.ProductNotFoundException;
import com.dev.productservice.models.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(long id) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(String name, double price, String description, String imageUrl, String category);
    Product replaceProduct(long id,String name, double price, String description, String imageUrl, String category);
}
