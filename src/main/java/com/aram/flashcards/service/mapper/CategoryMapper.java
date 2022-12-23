package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.dto.CategoryRequest;

public interface CategoryMapper {

    Category categoryFrom(CategoryRequest categoryRequest);

}
