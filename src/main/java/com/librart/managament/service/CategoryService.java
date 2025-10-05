package com.librart.managament.service;

import com.librart.managament.dto.CategoryDTO;
import com.librart.managament.response.ApiResponse;

import java.util.List;


public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(String categoryId);

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> searchCategoryByName(String categoryName);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, String categoryId);

    ApiResponse deleteCategory(String categoryId);

    List<CategoryDTO> findAllByOrderByCategoryIdAsc();
}
