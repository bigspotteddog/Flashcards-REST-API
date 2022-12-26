package com.aram.flashcards.repository;

import com.aram.flashcards.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Collection<Category> findAllByOrderByNameAsc();

    boolean existsByName(String name);

    Optional<Category> findByName(String name);

}
