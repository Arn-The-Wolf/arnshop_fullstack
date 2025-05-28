package com.arnshop.repository;

import com.arnshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByFeaturedTrue();

    boolean existsByName(String name);
}