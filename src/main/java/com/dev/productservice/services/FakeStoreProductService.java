package com.dev.productservice.services;

import com.dev.productservice.dtos.CreateFakeStoreProductRequestDto;
import com.dev.productservice.dtos.FakeStoreProductRequestDto;
import com.dev.productservice.dtos.FakeStoreResponseDto;
import com.dev.productservice.exceptions.ProductNotFoundException;
import com.dev.productservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{
    RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(long id) throws ProductNotFoundException {
        FakeStoreResponseDto fakeStoreResponseDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreResponseDto.class);

        if(fakeStoreResponseDto == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return fakeStoreResponseDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreResponseDto[] fakeStoreResponseDtos =
                restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreResponseDto[].class);

        List<Product> products = new ArrayList<>();
        for(FakeStoreResponseDto fakeStoreResponseDto : fakeStoreResponseDtos) {
            products.add(fakeStoreResponseDto.toProduct());
        }
        return products;
    }

    @Override
    public Product createProduct(String name, double price, String description, String imageUrl, String category) {
        FakeStoreProductRequestDto fakeStoreProductRequestDto = createDtoFromParam(name, price, description, imageUrl, category);
        FakeStoreResponseDto fakeStoreResponseDto = restTemplate.postForObject("https://fakestoreapi.com/products", fakeStoreProductRequestDto, FakeStoreResponseDto.class, String.class);
        return fakeStoreResponseDto.toProduct();
    }

    private FakeStoreProductRequestDto createDtoFromParam(String name, double price, String description, String imageUrl, String category) {
        FakeStoreProductRequestDto fakeStoreProductRequestDto = new FakeStoreProductRequestDto();
        fakeStoreProductRequestDto.setTitle(name);
        fakeStoreProductRequestDto.setPrice(price);
        fakeStoreProductRequestDto.setDescription(description);
        fakeStoreProductRequestDto.setImage(imageUrl);
        fakeStoreProductRequestDto.setCategory(category);
        return fakeStoreProductRequestDto;
    }

}
