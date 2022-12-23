package com.aram.flashcards.service.mapper;

import com.aram.flashcards.service.IdGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdGeneratorImplTest {

    @Test
    void generatedIdIsNotNull() {
        IdGenerator idGenerator = new IdGeneratorImpl();
        String id = idGenerator.generateId();
        assertNotNull(id);
    }

    @Test
    void generatedIdIsNotEmpty() {
        IdGenerator idGenerator = new IdGeneratorImpl();
        String id = idGenerator.generateId();
        assertFalse(id.trim().isEmpty());
    }

    @Test
    void generatedIdHasDesiredLength() {
        IdGenerator idGenerator = new IdGeneratorImpl();
        String id = idGenerator.generateId();
        assertEquals(36, id.length());
    }

}