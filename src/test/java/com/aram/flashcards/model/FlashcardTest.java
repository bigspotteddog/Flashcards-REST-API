package com.aram.flashcards.model;

import com.aram.flashcards.model.exception.InvalidEntityDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlashcardTest {

    private String id;
    private String studySessionId;
    private String question;
    private String answer;

    @BeforeEach
    void init() {
        this.id = "id";
        this.studySessionId = "studySessionId";
        this.question = "question";
        this.answer = "answer";
    }

    @Test
    void shouldThrowExceptionWithNullId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(null, studySessionId, question, answer));
    }

    @Test
    void shouldThrowExceptionWithEmptyId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard("", studySessionId, question, answer));
    }

    @Test
    void shouldThrowExceptionWithNullStudySessionId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, null, question, answer));
    }

    @Test
    void shouldThrowExceptionWithEmptyStudySessionId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, "", question, answer));
    }

    @Test
    void shouldThrowExceptionWithNullQuestion() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, studySessionId, null, answer));
    }

    @Test
    void shouldThrowExceptionWithEmptyQuestion() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, studySessionId, "", answer));
    }

    @Test
    void shouldThrowExceptionWithNullAnswer() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, studySessionId, question, null));
    }

    @Test
    void shouldThrowExceptionWithEmptyAnswer() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Flashcard(id, studySessionId, question, ""));
    }


}
