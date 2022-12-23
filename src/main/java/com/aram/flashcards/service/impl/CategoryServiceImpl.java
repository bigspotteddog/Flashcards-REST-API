package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.repository.CategoryRepository;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import com.aram.flashcards.service.exception.BadRequestException;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
class CategoryServiceImpl extends ValidatingService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(String id) {
        validateId(id);
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(format("Cannot find category with id = %s", id)));
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        validate(request);
        Category category = categoryFrom(request);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        validateId(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        validateId(id);
        return categoryRepository.existsById(id);
    }

    @Override
    public Category save(Category category) {
        assertNonNull(category);
        assertDoesNotExistByName(category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public void assertExistsById(String id) {
        if (!existsById(id)) {
            throw new NotFoundException(format("Cannot find category with id = %s", id));
        }
    }

    private void validate(CategoryRequest request) {
        assertNonNull(request);
        assertDoesNotExistByName(request.getName());
    }

    private void assertDoesNotExistByName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictException(format("Category with name = %s already exists", name));
        }
    }

    private Category categoryFrom(CategoryRequest request) {
        return categoryMapper.categoryFrom(request);
    }

}