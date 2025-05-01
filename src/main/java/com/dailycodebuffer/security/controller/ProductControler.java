package com.dailycodebuffer.security.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/product")
public class ProductControler {
    private record Product(Integer productId, String productName, double price){
    }

    List<Product> products = new ArrayList<>(
            List.of(
                    new Product(1,"Laptop",900),
                    new Product(2,"Iphone",500)
            )
    );

    @GetMapping
    public List<Product> getProducts(){
        return products;
    }
// dsadsad
    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        products.add(product);
        return product;
    }
}
