package com.aram.flashcards.controller;

import com.aram.flashcards.controller.error.ErrorResponse;
import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.StudySessionRequest;
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
@Tag(name = "Study Session")
@RequestMapping("/api/v1/study-sessions")
public class StudySessionController implements ResponseHandler {

    private final StudySessionService studySessionService;

    @Autowired
    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @Operation(summary = "GET all study sessions")
    @ApiResponse(
            responseCode = "200",
            description = "Found the study sessions",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudySession.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<StudySession>> findAll() {
        return ok(studySessionService.findAll());
    }

    @Operation(summary = "GET a study session by its id")
    @ApiResponse(
            responseCode = "200",
            description = "Found the study session",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudySession.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "Study session with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StudySession> findById(@PathVariable String id) {
        return ok(studySessionService.findById(id));
    }

    @Operation(summary = "GET all study sessions by category id")
    @ApiResponse(
            responseCode = "200",
            description = "Found the study sessions",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = StudySession.class))
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "Category with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @GetMapping("/details")
    public ResponseEntity<Iterable<StudySession>> findAllByCategoryId(@RequestParam String categoryId) {
        return ok(studySessionService.findAllByCategoryId(categoryId));
    }

    @Operation(summary = "POST a study session")
    @ApiResponse(
            responseCode = "201",
            description = "Created study session successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudySession.class)
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
            responseCode = "404",
            description = "Category with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PostMapping
    public ResponseEntity<StudySession> createStudySession(@Valid @RequestBody StudySessionRequest request) {
        return created(studySessionService.createStudySession(request));
    }

    @Operation(summary = "PUT a study session")
    @ApiResponse(
            responseCode = "200",
            description = "Updated study session successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudySession.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "201",
            description = "Created study session successfully",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudySession.class)
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
            responseCode = "404",
            description = "Category with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @PutMapping
    public ResponseEntity<StudySession> update(@Valid @RequestBody StudySession studySession) {
        return existsById(studySession.getId()) ? ok(save(studySession)) : created(save(studySession));
    }

    @Operation(summary = "DELETE a study session by its id")
    @ApiResponse(
            responseCode = "204",
            description = "Deleted study session successfully",
            content = {
                    @Content(
                            mediaType = "application/json"
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "Study session with specified id does not exist",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<StudySession> deleteById(@PathVariable String id) {
        studySessionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean existsById(String id) {
        return studySessionService.existsById(id);
    }

    private StudySession save(StudySession studySession) {
        return studySessionService.save(studySession);
    }

}
