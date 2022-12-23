package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CategoryMapperImpl implements CategoryMapper {

    private final IdGenerator idGenerator;

    @Autowired
    public CategoryMapperImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Category categoryFrom(CategoryRequest categoryRequest) {
        return new Category(
                idGenerator.generateId(),
                categoryRequest.getName()
        );
    }

}
