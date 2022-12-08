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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce.mail.EmailDetails;
import com.ecommerce.ecommerce.mail.EmailService;
import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.enums.UserRole;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.ProductRepository;
import com.ecommerce.ecommerce.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    // This method returns all the users in the database
    /*
     * GET /users/all
     * Returns all the users in the database
     * 
     * @return List<User> - List of all the users in the database
     * 
     * @throws 404 - If there is an error in the database
     * 
     * @param name (optional) - The name of the user in proper case
     *
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers(@RequestParam(required = false) String name) {
        try {
            List<User> users = new ArrayList<User>();

            if (name == null)
                userRepository.findAll().forEach(users::add);
            else
                userRepository.findByName(name).forEach(users::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>("[]", HttpStatus.OK);
            }
            for (User user : users) {
                user.setPassword(null);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method returns a user by id
    /*
     * GET/users/{id}
     * Returns a user by id
     * 
     * @return User - The user with the given id
     * 
     * @throws 500 - If there is an error in the database
     * 
     * @param id (long integer) - The id of the user
     * 
     * eg: http://localhost:8080/users/1
     * 
     * {
     * "id": 1,
     * "name": "Ansh Goyal",
     * "email": "email@gmail.com",
     * "password": null,
     * "phone": 9876543210,
     * "role": "ADMIN",
     * "balance": 100000.0
     * }
     *
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") long id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            userData.get().setPassword(null);
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

    /*
     * POST /users/edit/{id}
     * 
     * @return User - The user with the given id
     * 
     * @throws 500 - If there is an error in the database
     * 
     * @throws 404 - If the user is not found
     * 
     * In the body of the request, send the user object with the updated values.
     * - name
     * - password
     * - role
     * - phone
     * - balance
     * Right now only the name, role, phone and balance can be updated.
     * 
     * Setting Balance to 0 will not work.
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
            if (user.getName() != null) {
                _user.setName(user.getName());
            }
            if (user.getPassword() != null) {
                _user.setPassword(user.getPassword());
            }
            if (user.getRole() != null) {
                _user.setRole(user.getRole());
            }

            if (user.getPhone() != null && user.getPhone() != 0) {
                _user.setPhone(user.getPhone());
            }

            if (user.getBalance() != 0) {
                _user.setBalance(_user.getBalance() + user.getBalance());
            }

            if (user.getBalance() == -1) {
                _user.setBalance(0);
            }

            userRepository.save(_user);
            _user.setPassword(null);
            return new ResponseEntity<>(_user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // This method returns a user by email
    /*
     * POST /users/signup
     * Returns a user by email
     * 
     * @return User - The user with the given email
     * 
     * @throws 409 - If the email already exists
     * 
     * @throws 500 - If there is an error in the database
     * 
     * @param email (String) - The email of the user
     * 
     * eg: http://localhost:8080/user/signup
     * 
     * Request Body:
     * {
     * "name": "Ansh Goyal",
     * "email": "email@gmail.com"
     * "password": "password",
     * "phone": 9876543210
     * }
     * 
     * Response Body:
     * {
     * "id": 1,
     * "name": "Ansh Goyal",
     * "email": "email@gmail.com"
     * "password": null,
     * "phone": 9876543210,
     * "role": "USER",
     * "balance": 1000.0
     * }
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new ResponseEntity<>("The user already exisits", HttpStatus.CONFLICT);
            }
            User _user = userRepository.save(
                    new User(user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), UserRole.CUSTOMER));

            _user.setPassword(null);

            // Send email
            String subject = "Welcome to Shopify";
            String body = "Hello " + _user.getName() + ",\n\n"
                    + "Welcome to the Shopify. You can now start ordering your favoutite stuff directly from our website.\n\n"
                    + "Your account details are as follows:\n"
                    + "Email: " + _user.getEmail() + "\n"
                    + "Phone: " + _user.getPhone() + "\n\n"
                    + "Happy Shopping!!\n\n"
                    + "Regards,\n" + "Team Shopify";

            EmailDetails emailDetails = new EmailDetails(_user.getEmail(), body, subject, null);
            String status = emailService.sendSimpleMail(emailDetails);

            System.out.println("Email Status: " + status);

            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Sign in
    /*
     * POST /users/signin
     * Returns a user by email
     * 
     * @return User - The user with the given email
     * 
     * @throws 404 - If the user does not exist
     * 
     * @throws 500 - If there is an error in the database
     * 
     * Request Body:
     * {
     * "email": "
     * "password": "
     * }
     * 
     * Response Body:
     * {
     * "id": 1,
     * "name": "Ansh Goyal",
     * "email": "
     * "password": null,
     * "phone": 9876543210,
     * "role": "USER",
     * "balance": 1000.0
     * }
     */
    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody User user) {
        try {
            Optional<User> userData = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (userData.isPresent()) {
                userData.get().setPassword(null);

                return new ResponseEntity<>(userData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The user does not exist.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method deletes a user by id
    /*
     * DELETE /users/{id}
     * 
     * @return String - The message
     * 
     * @throws 500 - If there is an error in the database
     * 
     * @throws 404 - If the user is not found
     * 
     * @param id (long integer) - The id of the user
     * 
     * eg: http://localhost:8080/users/1
     * 
     * Response Body:
     * {
     * "message": "User deleted successfully!"
     * }
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        try {
            // First we delete all his orders
            Optional<User> userData = userRepository.findById(id);

            if (userData.isPresent()) {
                List<Order> orders = orderRepository.findByUser(userData.get());
                for (Order order : orders) {
                    // Increase the stock of the product
                    Product product = order.getProducts();
                    product.setQuantity(product.getQuantity() + order.getQuantity());
                    productRepository.save(product);

                    orderRepository.deleteById(order.getId());
                }
            } else {
                return new ResponseEntity<>("The user does not exist.", HttpStatus.NOT_FOUND);
            }

            userRepository.deleteById(id);

            return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
