package com.ecommerce.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import com.ecommerce.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByName(String name);

    // public List<Product> getAllProducts();

    public List<Product> findByNameContainingIgnoreCase(String name);

    public Optional<Product> findById(Long id);

}
