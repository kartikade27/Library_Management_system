package com.librart.managament.repository;

import com.librart.managament.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryNameIgnoreCase(String categoryName);

    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);

    List<Category> findAllByOrderByCategoryNameAsc();


}
