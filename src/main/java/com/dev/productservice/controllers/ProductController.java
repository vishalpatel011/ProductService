package com.dev.productservice.controllers;

import com.dev.productservice.dtos.CreateFakeStoreProductRequestDto;
import com.dev.productservice.dtos.ErrorDto;
import com.dev.productservice.dtos.ProductResponseDto;
import com.dev.productservice.exceptions.ProductNotFoundException;
import com.dev.productservice.models.Product;
import com.dev.productservice.services.FakeStoreProductService;
import com.dev.productservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        //List<ProductResponseDto> productResponseDtos = products.stream().map(ProductResponseDto::from).toList();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(ProductResponseDto.from(product));
        }
        return productResponseDtos;
    }

    @GetMapping("/products/{id}")
    public ProductResponseDto getProductbyId(@PathVariable("id") long id) throws ProductNotFoundException {
        Product product = productService.getProductById(id);
        return ProductResponseDto.from(product);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateFakeStoreProductRequestDto createFakeStoreProductRequestDto) {
        Product product = productService.createProduct(createFakeStoreProductRequestDto.getName(),
                                                        createFakeStoreProductRequestDto.getPrice(),
                                                        createFakeStoreProductRequestDto.getDescription(),
                                                        createFakeStoreProductRequestDto.getImageUrl(),
                                                        createFakeStoreProductRequestDto.getCategory());
        return new ResponseEntity<>(ProductResponseDto.from(product), HttpStatus.CREATED);
    }

}
