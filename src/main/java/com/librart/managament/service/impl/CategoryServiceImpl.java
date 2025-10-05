package com.librart.managament.service.impl;

import com.librart.managament.dto.CategoryDTO;
import com.librart.managament.exception.ResourceAlreadyExistsException;
import com.librart.managament.exception.ResourceNotFoundException;
import com.librart.managament.model.Category;
import com.librart.managament.repository.CategoryRepository;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.convertCategoryDTOToCategory(categoryDTO);
        if (this.categoryRepository.existsByCategoryNameIgnoreCase(categoryDTO.getCategoryName())) {
            throw new ResourceAlreadyExistsException("Category already exists!");
        }

        Category categorySaved = this.categoryRepository.save(category);
        return this.convertCategoryToCategoryDTO(categorySaved);
    }

    @Override
    public CategoryDTO getCategoryById(String categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return this.convertCategoryToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categoryList = Optional.ofNullable(this.categoryRepository.findAll())
                .orElse(Collections.emptyList());
        return categoryList.stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<CategoryDTO> searchCategoryByName(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return List.of();
        }
        List<Category> searchCategories = this.categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
        return searchCategories.stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, String categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        category.setCategoryName(categoryDTO.getCategoryName());
        Category categoryUpadated = this.categoryRepository.save(category);
        return this.convertCategoryToCategoryDTO(categoryUpadated);
    }

    @Override
    public ApiResponse deleteCategory(String categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        this.categoryRepository.delete(category);
        return ApiResponse.builder().message("Category Deleted!").build();
    }

    @Override
    public List<CategoryDTO> findAllByOrderByCategoryIdAsc() {
        List<Category> accendingOrderCategory = this.categoryRepository.findAllByOrderByCategoryNameAsc();
        if(accendingOrderCategory.isEmpty()){
            List.of();
        }
        return  accendingOrderCategory.stream()
                .map(this::convertCategoryToCategoryDTO)
                .collect(Collectors.toList());

    }

    private CategoryDTO convertCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName()).build();
    }

    private Category convertCategoryDTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .categoryName(categoryDTO.getCategoryName()).build();
    }
}
