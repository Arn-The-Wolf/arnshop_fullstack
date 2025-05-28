package com.arnshop.repository;

import com.arnshop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByFeaturedTrue();

    boolean existsByName(String name);
}