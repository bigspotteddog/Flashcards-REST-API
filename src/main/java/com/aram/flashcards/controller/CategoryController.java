package com.aram.flashcards.controller;

import com.aram.flashcards.controller.error.ErrorResponse;
import com.aram.flashcards.model.Category;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.CategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Category")
@RequestMapping("/api/v1/categories")
public class CategoryController implements ResponseHandler {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "GET all categories")
    @ApiResponse(
            responseCode = "200",
            description = "Found the categories",
            content = {
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Category.class))
                )
            }
    )
    @GetMapping
    ResponseEntity<Iterable<Category>> findAll() {
        return ok(categoryService.findAll());
    }

    @Operation(summary = "GET a category by its id")
    @ApiResponse(
            responseCode = "200",
            description = "Found the category",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
             }
    )
    @ApiResponse(
            responseCode = "404",
            description = "The category does not exist",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id) {
        return ok(categoryService.findById(id));
    }

    @Operation(summary = "GET a category by its name")
    @ApiResponse(
            responseCode = "200",
            description = "Found the category",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
            }
    )
    @ApiResponse(responseCode = "404",
                 description = "The category does not exist",
                 content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
                 }
    )
    @GetMapping("/details")
    public ResponseEntity<Category> findByName(@RequestParam String name) {
        return ok(categoryService.findByName(name));
    }

    @Operation(summary = "POST a category")
    @ApiResponse(
            responseCode = "201",
            description = "The category was created successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
            }
    )
    @ApiResponse(
            responseCode = "400",
            description = "Request body contains invalid data",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "409",
            description = "Category with specified name already exists",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        return created(categoryService.createCategory(request));
    }

    @Operation(summary = "PUT a category")
    @ApiResponse(
            responseCode = "200",
            description = "The category was updated successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "201",
            description = "Created a new category",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)
                    )
            }
    )
    @ApiResponse(responseCode = "400",
            description = "Request body contains invalid data",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "409",
            description = "Category with specified name already exists",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PutMapping
    public ResponseEntity<Category> update(@Valid @RequestBody Category category) {
        return existsById(category.getId()) ? ok(save(category)) : created(save(category));
    }

    @Operation(summary = "DELETE a category by its id")
    @ApiResponse(
            responseCode = "204",
            description = "The category was deleted successfully",
            content = {
                    @Content(
                            mediaType = "application/json"
                    )
            }
    )
    @ApiResponse(responseCode = "404",
            description = "The category does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteById(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean existsById(String id) {
        return categoryService.existsById(id);
    }

    private Category save(Category category) {
        return categoryService.save(category);
    }

}