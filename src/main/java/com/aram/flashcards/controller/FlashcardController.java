package com.aram.flashcards.controller;

import com.aram.flashcards.controller.error.ErrorResponse;
import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.dto.FlashcardRequest;
import com.aram.flashcards.service.FlashcardService;
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
@Tag(name = "Flashcard")
@RequestMapping("/api/v1/flashcards")
public class FlashcardController implements ResponseHandler {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @Operation(summary = "GET all flashcards")
    @ApiResponse(
            responseCode = "200",
            description = "Found the flashcards",
            content = {
                @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Flashcard.class))
                )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<Flashcard>> findAll() {
        return ok(flashcardService.findAll());
    }

    @Operation(summary = "GET a flashcard by id")
    @ApiResponse(
            responseCode = "200",
            description = "Found the flashcard",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Flashcard.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "The flashcard does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> findById(@PathVariable String id) {
        return ok(flashcardService.findById(id));
    }

    @Operation(summary = "GET all flashcards by study session id")
    @ApiResponse(
            responseCode = "200",
            description = "Found the flashcards",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Flashcard.class))
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "The study session with the specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @GetMapping("/details")
    public ResponseEntity<Iterable<Flashcard>> findAllByStudySessionId(@RequestParam String studySessionId) {
        return ok(flashcardService.findAllByStudySessionId(studySessionId));
    }

    @Operation(summary = "POST a flashcard")
    @ApiResponse(
            responseCode = "201",
            description = "Flashcard was created successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Flashcard.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "400",
            description = "The request body contains invalid data",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "The study session with the specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@Valid @RequestBody FlashcardRequest request) {
        return created(flashcardService.createFlashcard(request));
    }

    @Operation(summary = "PUT a flashcard")
    @ApiResponse(
            responseCode = "200",
            description = "Flashcard updated successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Flashcard.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "201",
            description = "Flashcard created successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Flashcard.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "400",
            description = "The request body contains invalid data",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "The study session with the specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PutMapping
    public ResponseEntity<Flashcard> update(@Valid @RequestBody Flashcard flashcard) {
        return existsById(flashcard.getId()) ? ok(save(flashcard)) : created(save(flashcard));
    }

    @Operation(summary = "DELETE a flashcard by its id")
    @ApiResponse(
            responseCode = "204",
            description = "Flashcard was deleted successfully",
            content = {
                    @Content(
                            mediaType = "application/json"
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "Flashcard with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Flashcard> deleteById(@PathVariable String id) {
        flashcardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean existsById(String id) {
        return flashcardService.existsById(id);
    }

    private Flashcard save(Flashcard flashcard) {
        return flashcardService.save(flashcard);
    }

}