package com.dev.productservice.services;

import com.dev.productservice.dtos.CreateFakeStoreProductRequestDto;
import com.dev.productservice.dtos.FakeStoreRequestDto;
import com.dev.productservice.dtos.FakeStoreResponseDto;
import com.dev.productservice.exceptions.ProductNotFoundException;
import com.dev.productservice.models.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
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
    public Product createProduct(String name, String description, double price,  String imageUrl, String category) {
        FakeStoreRequestDto fakeStoreProductRequestDto = createDtoFromParam(name, price, description, imageUrl, category);
        FakeStoreResponseDto fakeStoreResponseDto = restTemplate.postForObject("https://fakestoreapi.com/products", fakeStoreProductRequestDto, FakeStoreResponseDto.class, String.class);
        return fakeStoreResponseDto.toProduct();
    }

    @Override
    public Product replaceProduct(long id, String name, String description, double price, String imageUrl, String category) {
        FakeStoreRequestDto updatedfakeStoreRequestDto = createDtoFromParam(name, price, description, imageUrl, category);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FakeStoreRequestDto> requestEntity = new HttpEntity<>(updatedfakeStoreRequestDto, headers);
        ResponseEntity<FakeStoreResponseDto>  responseEntity = restTemplate.exchange("https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT, requestEntity, FakeStoreResponseDto.class);
        return responseEntity.getBody().toProduct();
    }

    @Override
    public Product patchProduct(long id, JsonPatch patch) throws ProductNotFoundException, JsonPatchException, JsonProcessingException {

        // Get existing product
        Product existingProduct = getProductById(id);

        // Convert Product to JSON Format
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode productNode = objectMapper.valueToTree(existingProduct);

        // Apply Patch
        JsonNode patchedNode = patch.apply(productNode);

        //Convert back to Product
        Product patchedProduct = objectMapper.treeToValue(patchedNode, Product.class);

        return replaceProduct(id,
                patchedProduct.getName(),
                patchedProduct.getDescription(),
                patchedProduct.getPrice(),
                patchedProduct.getCategory().getName(),
                patchedProduct.getImageUrl()
        );
    }


    private FakeStoreRequestDto createDtoFromParam(String name, double price, String description, String imageUrl, String category) {
        FakeStoreRequestDto fakeStoreProductRequestDto = new FakeStoreRequestDto();
        fakeStoreProductRequestDto.setTitle(name);
        fakeStoreProductRequestDto.setPrice(price);
        fakeStoreProductRequestDto.setDescription(description);
        fakeStoreProductRequestDto.setImage(imageUrl);
        fakeStoreProductRequestDto.setCategory(category);
        return fakeStoreProductRequestDto;

    }

}

