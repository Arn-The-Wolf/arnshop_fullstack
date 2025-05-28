package com.arnshop.service;

import com.arnshop.model.Product;
import com.arnshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrueAndActiveTrue();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public List<Product> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandIdAndActiveTrue(brandId);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
    }
}