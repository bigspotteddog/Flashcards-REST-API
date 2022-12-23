package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryMapperTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private CategoryMapperImpl categoryMapper;

    @Test
    void testCategoryFrom() {
        CategoryRequest request = new CategoryRequest("Music");

        when(idGenerator.generateId()).thenReturn("1");
        Category category = categoryMapper.categoryFrom(request);

        assertEquals("1", category.getId());
        assertEquals("Music", category.getName());
        verify(idGenerator, times(1)).generateId();
    }

}
