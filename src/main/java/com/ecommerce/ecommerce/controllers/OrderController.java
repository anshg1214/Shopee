package com.ecommerce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.Date;
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

@CrossOrigin
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
    public ResponseEntity<Object> getAllOrders(@RequestParam(required = false) String name) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (name == null)
                orderRepository.findAll().forEach(orders::add);
            else
                orderRepository.findByStatus(Status.valueOf(name)).forEach(orders::add);

            if (orders.isEmpty()) {
                return new ResponseEntity<>("[]", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method returns an order by id
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") long id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if (orderData.isPresent()) {

            // Hide the password
            Order order = orderData.get();
            order.getUser().setPassword(null);
            return new ResponseEntity<>(orderData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

    // This method returns an order by user id
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getOrderByUserId(@PathVariable("id") long id) {

        User user = userRepository.findById(id).get();

        if (user != null) {
            List<Order> orders = orderRepository.findByUser(user);
            for (Order order : orders) {
                order.getUser().setPassword("");
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getOrderByProductId(@PathVariable("id") long id) {

        Product product = producRepository.findById(id).get();

        if (product != null) {
            List<Order> orders = orderRepository.findByProduct(product);

            // Hide Password
            for (Order order : orders) {
                order.getUser().setPassword("");
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
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
            _order.setLastUpdated(new Date());
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
                order.getProducts().setQuantity(order.getProducts().getQuantity() + order.getQuantity());
                producRepository.save(order.getProducts());
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
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody Map<String, Object> body) {
        try {
            long userId = ((Integer) body.get("user")).longValue();
            List<Map<String, Object>> orderItems = (List<Map<String, Object>>) body.get("orderItems");

            List<Order> orders = new ArrayList<Order>();
            List<Map<String, Object>> orderItemsResponse = new ArrayList<Map<String, Object>>();
            // Get the user
            Optional<User> userData = userRepository.findById(userId);

            if (userData.isPresent()) {
                User user = userData.get();
                List<Product> products = new ArrayList<Product>();

                double balance = user.getBalance();

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
                            return new ResponseEntity<>(
                                    "Quantity " + " units for the product " + product.getName() + " is not available.",
                                    HttpStatus.BAD_REQUEST);
                        }

                        products.add(product);
                        orderItem.put("product", product);
                        orderItemsResponse.add(orderItem);
                        balance -= price * quantity;
                        if (balance < 0) {
                            return new ResponseEntity<>("Insufficient Balance", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
                    }
                }

                // Check if the user has enough balance
                if (balance < 0) {
                    return new ResponseEntity<>("Insufficient Balance", HttpStatus.BAD_REQUEST);
                }

                for (Map<String, Object> orderItem : orderItemsResponse) {

                    int quantity = (int) orderItem.get("quantity");
                    int price = (int) orderItem.get("price");
                    Product product = (Product) orderItem.get("product");

                    // Update the quantity of the product
                    product.setQuantity(product.getQuantity() - quantity);
                    producRepository.save(product);

                    // Create the order
                    Order order = new Order(user, product, price, quantity);
                    orderRepository.save(order);
                    orders.add(order);

                }

                user.setBalance(balance);
                userRepository.save(user);

            } else {
                return new ResponseEntity<>("The user does not exist.", HttpStatus.NOT_FOUND);
            }

            // Hide Password
            for (Order order : orders) {
                order.getUser().setPassword("");
            }
            return new ResponseEntity<>(orders, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}