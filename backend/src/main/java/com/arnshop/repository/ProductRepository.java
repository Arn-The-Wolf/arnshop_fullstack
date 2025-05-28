package com.arnshop.repository;

import com.arnshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByFeaturedTrueAndActiveTrue();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String query);
}