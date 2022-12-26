package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.repository.CategoryRepository;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.lang.String.format;

@Service
@Transactional
class CategoryServiceImpl extends ValidatingService implements CategoryService {

    private static final String CANNOT_FIND_BY_ID = "Cannot find category with id = %s";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    CategoryServiceImpl(CategoryRepository categoryRepository,
                        CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Collection<Category> findAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(format(CANNOT_FIND_BY_ID, id)));
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        validate(request);
        Category category = categoryMapper.categoryFrom(request);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        assertExistsById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public Category save(Category category) {
        validate(category);
        return categoryRepository.save(category);
    }

    @Override
    public void assertExistsById(String id) {
        if (!existsById(id)) {
            throw new NotFoundException(format("Cannot find category with id = %s", id));
        }
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(format("Cannot find category with name = %s", name)));
    }

    private void validate(Category category) {
        assertNotNull(category);
        assertDoesNotExistByName(category.getName());
    }

    private void validate(CategoryRequest request) {
        assertNotNull(request);
        assertDoesNotExistByName(request.getName());
    }

    private void assertDoesNotExistByName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictException(format("Category with name = %s already exists", name));
        }
    }

}