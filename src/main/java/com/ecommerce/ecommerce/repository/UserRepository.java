package com.ecommerce.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);

    List<User> findByName(String name);

    List<User> findByNameContaining(String name);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    Optional<User> findByEmailAndPassword(String email, String password);

}
