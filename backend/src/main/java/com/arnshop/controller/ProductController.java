package com.arnshop.controller;

import com.arnshop.model.Product;
import com.arnshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        return ResponseEntity.ok(productService.getFeaturedProducts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(productService.getProductsByBrand(brandId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }
}