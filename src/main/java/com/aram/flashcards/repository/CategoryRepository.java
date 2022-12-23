package com.aram.flashcards.repository;

import com.aram.flashcards.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
}
