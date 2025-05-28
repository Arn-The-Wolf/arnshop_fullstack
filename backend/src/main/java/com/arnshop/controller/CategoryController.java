package com.arnshop.controller;

import com.arnshop.model.Category;
import com.arnshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8000")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Category>> getFeaturedCategories() {
        return ResponseEntity.ok(categoryRepository.findByFeaturedTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found")));
    }
}