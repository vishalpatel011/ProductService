package com.dev.productservice.repositories;

import com.dev.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);

    Optional<Product> findById(long id);

    List<Product> findAll();

}
