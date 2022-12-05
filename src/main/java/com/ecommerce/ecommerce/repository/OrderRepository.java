package com.ecommerce.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.ecommerce.ecommerce.enums.Status;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public Optional<Order> findById(Long id);

    public List<Order> findByUser(User user);

    public List<Order> findByStatus(Status status);
    
    public List<Order> findByProduct(Product product);

}
