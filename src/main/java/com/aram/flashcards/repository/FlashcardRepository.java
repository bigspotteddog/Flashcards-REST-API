package com.aram.flashcards.repository;

import com.aram.flashcards.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, String> {

    Iterable<Flashcard> findAllByStudySessionId(String studySessionId);

}
