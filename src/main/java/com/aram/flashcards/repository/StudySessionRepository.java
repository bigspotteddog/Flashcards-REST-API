package com.aram.flashcards.repository;

import com.aram.flashcards.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, String> {

    Optional<StudySession> findByName(String name);

    Iterable<StudySession> findAllByCategoryId(String id);

}