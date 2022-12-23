package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.repository.CategoryRepository;
import com.aram.flashcards.service.dto.CategoryRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Category> categories;

    private Category category;

    private CategoryRequest request;

    @BeforeEach
    void init() {
        this.request = new CategoryRequest("Music");
        this.category = new Category("1", "Music");
        this.categories = List.of(category);
    }

    @Test
    void testFindAll() {
        when(categoryRepository.findAll()).thenReturn(categories);
        assertEquals(categories, categoryService.findAll());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById("1")).thenReturn(Optional.of(category));
        assertEquals(category, categoryService.findById("1"));
        verify(categoryRepository, times(1)).findById("1");
    }

    @Test
    void findByIdThrowsExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> categoryService.findById("1"));
        verify(categoryRepository, times(1)).findById("1");
    }

    @Test
    void cannotCreateCategoryWithDuplicateName() {
        when(categoryRepository.existsByName("Music")).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> categoryService.createCategory(new CategoryRequest("Music")));

        verify(categoryRepository, times(1)).existsByName("Music");
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryMapper.categoryFrom(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        assertEquals(categoryService.createCategory(request), category);
        verify(categoryRepository, times(1)).existsByName(request.getName());
        verify(categoryMapper, times(1)).categoryFrom(request);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteById() {
        categoryService.deleteById("1");
        verify(categoryRepository, times(1)).deleteById("1");
    }

    @Test
    void testSave() {
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.save(category));
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testExistsById() {
        when(categoryRepository.existsById("1")).thenReturn(true);

        assertTrue(categoryService.existsById("1"));
        verify(categoryRepository, times(1)).existsById("1");
    }

    @Test
    void assertExistsByIdShouldThrowExceptionWhenCategoryDoesNotExistById() {
        when(categoryRepository.existsById("1")).thenReturn(false);
        assertThrows(NotFoundException.class, () -> categoryService.assertExistsById("1"));
    }

    @Test
    void assertExistsByIdShouldNotThrowExceptionWhenCategoryExistsById() {
        when(categoryRepository.existsById("1")).thenReturn(true);
        assertDoesNotThrow(() -> categoryService.assertExistsById("1"));
    }

}
