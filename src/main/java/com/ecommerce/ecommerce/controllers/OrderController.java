package com.ecommerce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestBody;
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
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> getOrderByUserId(@PathVariable("id") long id) {
        List<Order> orderData = orderRepository.findByUser(id);

        if (orderData != null) {
            return new ResponseEntity<>(orderData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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

    // This method updates an order
    @PostMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") long id, @RequestBody Order order) {
        Optional<Order> orderData = orderRepository.findById(id);
        if (orderData.isPresent()) {
            Order _order = orderData.get();
            if (order.getStatus() != null) {
                _order.setStatus(order.getStatus());
            }
            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Order
    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") long id) {
        try {
            // Update the quantity of the products
            Optional<Order> orderData = orderRepository.findById(id);
            if (orderData.isPresent()) {
                Order order = orderData.get();
                order.getOrderItems().setQuantity(order.getOrderItems().getQuantity() + order.getQuantity());
                producRepository.save(order.getOrderItems());
            }

            orderRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create Order
    /*
     * The body of the request must be like this:
     * {
     * "user": 1,
     * "orderItems": [
     * {
     * "id": 1,
     * "quantity": 1
     * "price": 100
     * },
     * {
     * "id": 2,
     * "quantity": 1
     * "price": 100
     * 
     * ]
     * }
     */
    // When we create an order, we split the order such that we have one order for
    // each product

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody Map<String, Object> body) {
        try {
            long userId = ((Integer) body.get("user")).longValue();
            List<Map<String, Object>> orderItems = (List<Map<String, Object>>) body.get("orderItems");

            List<Order> orders = new ArrayList<Order>();
            // Get the user
            Optional<User> userData = userRepository.findById(userId);
            if (userData.isPresent()) {
                User user = userData.get();

                // Create the order
                for (Map<String, Object> orderItem : orderItems) {
                    long productId = ((Integer) orderItem.get("id")).longValue();
                    int quantity = (int) orderItem.get("quantity");
                    int price = (int) orderItem.get("price");

                    // Get the product
                    Optional<Product> productData = producRepository.findById(productId);
                    if (productData.isPresent()) {
                        Product product = productData.get();

                        // Check if the quantity is available
                        if (product.getQuantity() < quantity) {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        // Create the order
                        Order order = new Order(user, product, price, quantity);
                        orderRepository.save(order);
                        orders.add(order);

                        // Update the quantity of the product
                        product.setQuantity(product.getQuantity() - quantity);
                        producRepository.save(product);
                    }
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(orders, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}