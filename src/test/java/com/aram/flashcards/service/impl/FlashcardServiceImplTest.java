package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.repository.FlashcardRepository;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.FlashcardRequest;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.FlashcardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashcardServiceImplTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private StudySessionService studySessionService;

    @Mock
    private FlashcardMapper mapper;

    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    private Flashcard flashcard;

    private List<Flashcard> flashcards;

    @BeforeEach
    void init() {
        String id = "1";
        String studySessionId = "2";
        this.flashcard = new Flashcard(
                id,
                studySessionId,
                "What is the color of the sky",
                "Blue"
        );
        this.flashcards = List.of(flashcard);
    }

    @Test
    void testFindAll() {
        when(flashcardRepository.findAll()).thenReturn(flashcards);

        assertEquals(flashcards, flashcardService.findAll());
        verify(flashcardRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(flashcardRepository.findById("1")).thenReturn(Optional.of(flashcard));

        assertEquals(flashcard, flashcardService.findById("1"));
        verify(flashcardRepository, times(1)).findById("1");
    }

    @Test
    void throwsExceptionWhenFlashcardDoesNotExistById() {
        when(flashcardRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flashcardService.findById("1"));
        verify(flashcardRepository, times(1)).findById("1");
    }

    @Test
    void findsAllByStudySessionId() {
        when(flashcardRepository.findAllByStudySessionId("1"))
                .thenReturn(Set.of(flashcard));

        assertEquals(Set.of(flashcard), flashcardService.findAllByStudySessionId("1"));
        verify(flashcardRepository, times(1)).findAllByStudySessionId("1");
    }

    @Test
    void throwsExceptionWhenCreatingFlashcardWithNonExistentStudySession() {
        String studySessionId = "1";
        FlashcardRequest request = new FlashcardRequest(
                studySessionId,
                "What is the color of the sky?",
                "Blue"
        );

        doThrow(new NotFoundException(format("Cannot find study session with id = %s", studySessionId)))
                .when(studySessionService).assertExistsById(studySessionId);

        assertThrows(NotFoundException.class, () -> flashcardService.createFlashcard(request));
        verify(studySessionService, times(1)).assertExistsById("1");
    }

    @Test
    void testCreateFlashcard() {
        String studySessionId = "1";
        FlashcardRequest request = new FlashcardRequest(
                studySessionId,
                "What is the color of the sky?",
                "Blue"
        );
        Flashcard flashcard = new Flashcard(
                "id",
                studySessionId,
                "What is the color of the sky?",
                "Blue"
        );

        doNothing().when(studySessionService).assertExistsById("1");
        when(mapper.flashcardFrom(request)).thenReturn(flashcard);
        when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

        assertEquals(flashcard, flashcardService.createFlashcard(request));
        verify(mapper, times(1)).flashcardFrom(request);
        verify(studySessionService, times(1)).assertExistsById("1");
        verify(flashcardRepository, times(1)).save(flashcard);
    }

    @Test
    void testExistsById() {
        when(flashcardRepository.existsById("1")).thenReturn(true);

        assertTrue(flashcardService.existsById("1"));
        verify(flashcardRepository, times(1)).existsById("1");
    }

    @Test
    void testSave() {
        when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

        assertEquals(flashcard, flashcardService.save(flashcard));
        verify(flashcardRepository, times(1)).save(flashcard);
    }

    @Test
    void testDeleteById() {
        flashcardService.deleteById("1");

        verify(flashcardRepository, times(1)).deleteById("1");
    }

}
