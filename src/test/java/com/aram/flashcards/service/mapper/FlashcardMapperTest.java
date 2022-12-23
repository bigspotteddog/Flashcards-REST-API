package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.FlashcardRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashcardMapperTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private FlashcardMapperImpl flashcardMapper;

    @Test
    void testFlashcardFrom() {
        String studySessionId = "2";
        FlashcardRequest request = new FlashcardRequest(
                studySessionId,
                "What is the color of the sky?",
                "Blue"
        );

        when(idGenerator.generateId()).thenReturn("1");
        Flashcard flashcard = flashcardMapper.flashcardFrom(request);

        assertEquals("1", flashcard.getId());
        assertEquals("2", flashcard.getStudySessionId());
        assertEquals("What is the color of the sky?", flashcard.getQuestion());
        assertEquals("Blue", flashcard.getAnswer());
        verify(idGenerator, times(1)).generateId();
    }

}
