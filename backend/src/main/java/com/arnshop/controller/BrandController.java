package com.arnshop.controller;

import com.arnshop.model.Brand;
import com.arnshop.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BrandController {

    private final BrandRepository brandRepository;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Brand>> getFeaturedBrands() {
        return ResponseEntity.ok(brandRepository.findByFeaturedTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found")));
    }
}