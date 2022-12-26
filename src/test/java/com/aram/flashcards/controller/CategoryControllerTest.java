package com.aram.flashcards.controller;

import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest extends WebLayerTest {

    @Value("${spring.servlet.path.categories}")
    private String categoriesPath;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    private Category category;

    @BeforeEach()
    void init() {
        this.category = new Category("1", "Music");
    }

    @Test
    void findsAllCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(Set.of(category));

        mockMvc.perform(get(categoriesPath)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(Set.of(category))));
    }

    @Test
    void findsCategoryById() throws Exception {
        when(categoryService.findById("1")).thenReturn((new Category("1", "Music")));

        mockMvc.perform(get(categoriesPath + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':'1', 'name':'Music'}"));
    }

    @Test
    void returnsNotFoundWhenCategoryDoesNotExistById() throws Exception {
        String id = "1";
        String errorMessage = format("Cannot find category with id = %s", id);
        when(categoryService.findById(id)).thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(get(categoriesPath + "/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(content().json("{'message':'Cannot find category with id = 1'}"));
    }

    @Test
    void findsCategoryByName() throws Exception {
        when(categoryService.findByName("Music")).thenReturn(new Category("1", "Music"));

        mockMvc.perform(get(categoriesPath + "/details?name=Music")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':'1','name':'Music'}"));
    }

    @Test
    void returnsNotFoundWhenCategoryDoesNotExistByName() throws Exception {
        when(categoryService.findByName("Music"))
                .thenThrow(new NotFoundException("Cannot find category with name = Music"));

        mockMvc.perform(get(categoriesPath + "/details?name=Music")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'Cannot find category with name = Music'}"));
    }

    @Test
    void createsCategory() throws Exception {
        when(categoryService.createCategory(new CategoryRequest("Music")))
                .thenReturn(new Category("1", "Music"));

        String expectedResponseBody = """
                {
                    "id":"1",
                    "name":"Music"
                }
                """;

        mockMvc.perform(post(categoriesPath)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Music\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void returnsBadRequestWhenRequestBodyToCreateCategoryIsEmpty() throws Exception {
        mockMvc.perform(post(categoriesPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsConflictWhenCategoryAlreadyExistsByName() throws Exception {
        CategoryRequest request = new CategoryRequest("Music");
        String errorMessage = "Category with name = Music already exists";

        when(categoryService.createCategory(request))
                .thenThrow(new ConflictException(errorMessage));

        mockMvc.perform(post(categoriesPath)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"Music\"}"))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(content().json("{'message':'Category with name = Music already exists'}"));
    }

    @Test
    void updatesCategoryWithStatusCreatedWhenCategoryDoesNotExistById() throws Exception {
        Category category = new Category("1", "Jazz Music");
        when(categoryService.existsById("1")).thenReturn(false);
        when(categoryService.save(category)).thenReturn(category);

        String requestBody = """
                {
                    "id": "1",
                    "name": "Jazz Music"
                }
                """;

        String expectedResponseBody = """
                {
                    "id": "1",
                    "name": "Jazz Music"
                }
                """;

        mockMvc.perform(put(categoriesPath)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void updatesCategoryWithStatusOkWhenCategoryExistsById() throws Exception {
        Category category = new Category("1", "Jazz Music");
        when(categoryService.existsById("1")).thenReturn(true);
        when(categoryService.save(category)).thenReturn(category);

        String requestBody = """
                {
                    "id": "1",
                    "name": "Jazz Music"
                }
                """;

        String expectedResponseBody = """
                {
                    "id": "1",
                    "name": "Jazz Music"
                }
                """;

        mockMvc.perform(put(categoriesPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    @Test
    void returnsBadRequestWhenRequestBodyToUpdateCategoryIsEmpty() throws Exception {
        mockMvc.perform(put(categoriesPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletesCategoryById() throws Exception {
        mockMvc.perform(delete(categoriesPath + "/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById("1");
    }

}