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

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.repository.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    // This method returns all the products in the database
    /*
     * GET /products/all
     * Returns all the products in the database
     * 
     * @return List<Product> - List of all the products in the database
     * 
     * @throws 404 - If there is an error in the database
     * 
     * @Throws 204 - If there are no products in the database
     * 
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts(@RequestParam(required = false) String name) {

        try {
            List<Product> products = new ArrayList<Product>();

            if (name == null)
                productRepository.findAll().forEach(products::add);
            else
                productRepository.findByName(name).forEach(products::add);

            if (products.isEmpty()) {
                return new ResponseEntity<>("[]", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method returns a product by id
    /*
     * GET /products/id/{id}
     * Returns a product by id
     * 
     * @param id - The id of the product
     * 
     * @return Product - The product with the given id
     * 
     * @throws 404 - If there is an error in the database
     * 
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") long id) {
        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()) {
            return new ResponseEntity<>(productData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

    // This method returns a product by name
    /*
     * GET /products/name/{name}
     * Returns a product by name
     * 
     * @param name - The name of the product
     * 
     * @return Product - The product with the given name
     * 
     * @throws 204 - If there is an error in the database
     * 
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<Object> findByNameContaining(@PathVariable String name) {
        try {
            List<Product> products = productRepository.findByNameContaining(name);

            if (products.isEmpty()) {
                return new ResponseEntity<>("[]", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method edits a product
    /*
     * POST /products/edit/{id}
     * Edits a product by id
     * 
     * @param id - The id of the product
     * 
     * @RequestBody
     * - name (optional)
     * - description (optional)
     * - price (optional)
     * 
     * @return Product - The edited product
     * 
     * @throws 404 - If there is an error in the database
     * 
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            Product _product = productData.get();
            if (product.getName() != null) {
                _product.setName(product.getName());
            }
            if (product.getDescription() != null) {
                _product.setDescription(product.getDescription());
            }
            if (product.getPrice() != 0) {
                _product.setPrice(product.getPrice());
            }
            // What if the quantity is 0?
            // if (product.getQuantity() != 0) {
            // _product.setQuantity(product.getQuantity());
            // }

            return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a product by id
    /*
     * POST /products/delete/{id}
     * Deletes a product by id
     * 
     * @param id - The id of the product
     * 
     * Returns 200 - If the product is deleted
     * 
     * Returns 500 - If there is an error in the database or the product does not
     * exist
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product _product = productRepository.save(new Product(product.getName(), product.getPrice(),
                    product.getDiscount(), product.getDescription(), product.getQuantity(), product.getImage(),
                    product.getDelivery()));
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
