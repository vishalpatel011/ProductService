package com.dev.productservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private long id;
    private String name;
    private String description;
    private String imageUrl;
    private Category category;
    private double price;
}

