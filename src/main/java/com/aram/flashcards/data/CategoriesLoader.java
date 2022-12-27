package com.aram.flashcards.data;

import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CategoriesLoader {

    @Autowired
    private CategoryService categoryService;

    void loadCategories() {
        categories().stream()
                .map(CategoryRequest::new)
                .forEach(categoryService::createCategory);
    }

    private List<String> categories() {
        return List.of(
                "Art",
                "Biology",
                "Chemistry",
                "Cinema",
                "Engineering",
                "Graphic design",
                "History",
                "Math",
                "Medicine",
                "Music",
                "Physics",
                "Sports",
                "Web development"
        );
    }

}
