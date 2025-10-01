package com.furniturehome.controller;

import com.furniturehome.dto.CategoryDTO;
import com.furniturehome.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
    		@RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "description", required = false) String description
    ) {
        CategoryDTO dto = categoryService.createCategory(name, description);
        return ResponseEntity
                .created(URI.create("/api/categories/" + dto.getId()))
                .body(dto);
    }

    // Get single category
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id)); // 200 OK
    }

    // Get all categories
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> listCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories()); // 200 OK
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable("id") Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, name, description)); // 200 OK
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
