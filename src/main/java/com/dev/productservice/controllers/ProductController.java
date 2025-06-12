package com.dev.productservice.controllers;

import com.dev.productservice.dtos.ErrorDto;
import com.dev.productservice.dtos.ProductResponseDto;
import com.dev.productservice.models.Product;
import com.dev.productservice.services.FakeStoreProductService;
import com.dev.productservice.services.ProductService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ProductResponseDto getProductbyId(@PathVariable("id") long id){
        Product product = productService.getProductById(id);
        return ProductResponseDto.from(product);
    }

    @ExceptionHandler(NullPointerException.class)
    public ErrorDto handleNullPointerException() {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Product not found");
        errorDto.setStatus("Failure");
        return errorDto;
    }
}
