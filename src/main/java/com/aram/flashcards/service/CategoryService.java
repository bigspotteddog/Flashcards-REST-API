package com.aram.flashcards.service;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.dto.CategoryRequest;

public interface CategoryService {

    Iterable<Category> findAll();

    Category findById(String id);

    Category createCategory(CategoryRequest request);

    void deleteById(String id);

    boolean existsById(String id);

    Category save(Category category);

    void assertExistsById(String id);

    Category findByName(String name);

}
