package com.ecommerce.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce.repository.ProductRepository;

import com.ecommerce.ecommerce.enums.Status;
import com.ecommerce.ecommerce.enums.UserRole;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.repository.OrderRepository;

import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    // This method returns all the orders in the database
    /*
     * GET /admin/all
     * Returns all the orders in the database
     * 
     * @return List<Order> - List of all the orders in the database
     * 
     * @throws 404 - If there is an error in the database
     * 
     * @param name (optional) - The name of the order in proper case
     *
     */
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

    // This method returns a sales statistics for all the orders
    /*
     * GET /admin/reports
     * Returns a sales statistics for all the orders
     * 
     * @return Map<String, Object> - A map containing the sales statistics
     * 
     * @throws 404 - If there is an error in the database
     *
     */
    @GetMapping("/reports")
    public ResponseEntity<Object> getSalesReport() {

        try {
            Map<String, Object> salesReport = new HashMap<String, Object>();

            long totalOrders = orderRepository.count();
            long totalProducts = productRepository.count();
            double totalSales = orderRepository.findAll().stream().mapToDouble(Order::getTotalPrice).sum();
            long totalUsers = userRepository.count();

            // This contains the number of orders for each status
            Map<String, Integer> orderStatistics = new HashMap<String, Integer>();
            Map<String, Double> orderAmount = new HashMap<String, Double>();
            Map<String, Integer> userStatistics = new HashMap<String, Integer>();

            for (Status status : Status.values()) {
                List<Order> orders = orderRepository.findByStatus(status);
                orderStatistics.put(status.toString(), orders.size());
                orderAmount.put(status.toString(), orders.stream().mapToDouble(Order::getTotalPrice).sum());
            }

            for (UserRole role : UserRole.values()) {
                List<User> users = userRepository.findByRole(role);
                userStatistics.put(role.toString(), users.size());
            }

            salesReport.put("totalOrders", totalOrders);
            salesReport.put("totalProducts", totalProducts);
            salesReport.put("totalUsers", totalUsers);
            salesReport.put("totalSales", totalSales);
            salesReport.put("userStatistics", userStatistics);
            salesReport.put("orderStatistics", orderStatistics);
            salesReport.put("orderAmount", orderAmount);

            return new ResponseEntity<>(salesReport, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
