package com.aram.flashcards.controller;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.dto.FlashcardRequest;
import com.aram.flashcards.service.FlashcardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/flashcards")
public class FlashcardController implements ResponseHandler {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Flashcard>> findAll() {
        return ok(flashcardService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> findById(@PathVariable String id) {
        return ok(flashcardService.findById(id));
    }

    @GetMapping("/details")
    public ResponseEntity<Iterable<Flashcard>> findAllByStudySessionId(@RequestParam String studySessionId) {
        return ok(flashcardService.findAllByStudySessionId(studySessionId));
    }

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@Valid @RequestBody FlashcardRequest request) {
        return created(flashcardService.createFlashcard(request));
    }

    @PutMapping
    public ResponseEntity<Flashcard> update(@Valid @RequestBody Flashcard flashcard) {
        return existsById(flashcard.getId()) ? ok(save(flashcard)) : created(save(flashcard));
    }

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