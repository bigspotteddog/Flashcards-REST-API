package com.aram.flashcards.repository;

import com.aram.flashcards.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySession, String> {
    boolean existsByName(String name);
}
