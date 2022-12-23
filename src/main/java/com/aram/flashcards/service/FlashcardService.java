package com.aram.flashcards.service;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.dto.FlashcardRequest;

public interface FlashcardService {
    Iterable<Flashcard> findAll();

    Flashcard findById(String id);

    Flashcard createFlashcard(FlashcardRequest request);

    boolean existsById(String id);

    Flashcard save(Flashcard flashcard);

    void deleteById(String id);

}
