package com.furniturehome.service;

import com.furniturehome.dto.CategoryDTO;
import com.furniturehome.model.Category;
import com.furniturehome.exception.ConflictException;
import com.furniturehome.exception.ResourceNotFoundException;
import com.furniturehome.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        return toDTO(category);
    }

    public CategoryDTO createCategory(String name, String description) {
        categoryRepository.findByName(name).ifPresent(existing -> {
            throw new ConflictException("Category with name '" + name + "' already exists");
        });

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return toDTO(categoryRepository.save(category));
    }

    public CategoryDTO updateCategory(Long id, String name, String description) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        categoryRepository.findByName(name)
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ConflictException("Category with name '" + name + "' already exists");
                });

        if (name != null) existing.setName(name);
        if (description != null) existing.setDescription(description);

        return toDTO(categoryRepository.save(existing));
    }

    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(existing);
    }
}
