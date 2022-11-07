package com.ecommerce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce.enums.Status;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.Product;

import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:9000")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository producRepository;

    // This method returns all the orders in the database
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(required = false) String name) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (name == null)
                orderRepository.findAll().forEach(orders::add);
            else
                orderRepository.findByStatus(Status.valueOf(name)).forEach(orders::add);

            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method returns an order by id
    @GetMapping("/id/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") long id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if (orderData.isPresent()) {
            return new ResponseEntity<>(orderData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // This method returns an order by user id
    // @GetMapping("/user/{id}")
    // public ResponseEntity<List<Order>> getOrderByUserId(@PathVariable("id") long id) {
    //     List<Order> orderData = orderRepository.findByUser(id);

    //     if (orderData != null) {
    //         return new ResponseEntity<>(orderData, HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

    // This method returns an order by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrderByStatus(@PathVariable("status") String status) {
        List<Order> orderData = orderRepository.findByStatus(Status.valueOf(status));

        if (orderData != null) {
            return new ResponseEntity<>(orderData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new order
    // @PostMapping("/create")
    // public ResponseEntity<Order> createOrder(@RequestParam(required = true) long userId,
    //         @RequestParam(required = true) Status status,
    //         @RequestParam(required = true) Long userID,
    //         @RequestParam(required = true) double totalPrice,
    //         @RequestParam(required = true) Long productID) {
    //     try {
    //         Optional<User> user = userRepository.findById(userID);
    //         Optional<Product> product = producRepository.findById(productID);

    //         if (!user.isPresent()) {
    //             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //         }
    //         Order _order = new Order(user.get(), product.get(), totalPrice, status);
    //         orderRepository.save(_order);
    //         return new ResponseEntity<>(_order, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}
