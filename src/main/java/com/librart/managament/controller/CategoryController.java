package com.librart.managament.controller;

import com.librart.managament.dto.CategoryDTO;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/createCategory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = this.categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/getCategoryById/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
        CategoryDTO categoryById = this.categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryById);
    }

    @GetMapping("/getAllCategory")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' , 'USER')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> allCategories = this.categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/searchCategory")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN' ,'USER')")
    public ResponseEntity<List<CategoryDTO>> searchCategoryByName(@RequestParam String categoryName) {
        List<CategoryDTO> categoryDTOS = this.categoryService.searchCategoryByName(categoryName);
        return ResponseEntity.ok(categoryDTOS);
    }

    @PutMapping("/updateCategory/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable String categoryId) {
        CategoryDTO categoryDTO1 = this.categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.ok(categoryDTO1);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId) {
        ApiResponse apiResponse = this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/findAllShortedCategory")
    @PreAuthorize("hasAnyRole('ADMIN' , 'LIBRARIAN','USER')")
    public ResponseEntity<List<CategoryDTO>> findAllByOrderByCategoryIdAsc() {
        List<CategoryDTO> allByOrderByCategoryIdAsc = this.categoryService.findAllByOrderByCategoryIdAsc();
        return ResponseEntity.ok(allByOrderByCategoryIdAsc);
    }
}
