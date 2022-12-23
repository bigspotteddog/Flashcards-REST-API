package com.aram.flashcards.controller;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController implements ResponseHandler {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    ResponseEntity<Iterable<Category>> findAll() {
        return ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id) {
        return ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        return created(categoryService.createCategory(request));
    }

    @PutMapping
    public ResponseEntity<Category> update(@RequestBody Category category) {
        return existsById(category.getId()) ? ok(save(category)) : created(save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteById(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean existsById(String id) {
        return categoryService.existsById(id);
    }

    private Category save(Category category) {
        return categoryService.save(category);
    }

}
