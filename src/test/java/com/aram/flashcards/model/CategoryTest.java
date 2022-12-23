package com.aram.flashcards.model;

import com.aram.flashcards.model.exception.InvalidEntityDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryTest {

    @Test
    void shouldThrowExceptionWithNullId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Category(null, "name"));
    }

    @Test
    void shouldThrowExceptionWithEmptyId() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Category("", "name"));
    }

    @Test
    void shouldThrowExceptionWithNullName() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Category("id", null));
    }

    @Test
    void shouldThrowExceptionWithEmptyName() {
        assertThrows(InvalidEntityDataException.class,
                () -> new Category("id", ""));
    }

}
