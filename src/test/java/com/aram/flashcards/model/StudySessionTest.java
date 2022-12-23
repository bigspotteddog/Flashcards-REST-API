package com.aram.flashcards.model;

import com.aram.flashcards.model.exception.InvalidEntityDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudySessionTest {

    @Test
    void shouldThrowExceptionWithNullId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession(null, "categoryId", "name"));
    }

    @Test
    void shouldThrowExceptionWithEmptyId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession("", "categoryId", "name"));
    }

    @Test
    void shouldThrowExceptionWithNullCategoryId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession("id", null, "name"));
    }

    @Test
    void shouldThrowExceptionWithEmptyCategoryId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession("id", "", "name"));
    }

    @Test
    void shouldThrowExceptionWithNullName() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession("id", "categoryId", null));
    }

    @Test
    void shouldThrowExceptionWithEmptyName() {
        assertThrows(InvalidEntityDataException.class,
                () -> new StudySession("id", "categoryId", ""));
    }

}
