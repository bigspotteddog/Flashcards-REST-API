package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.repository.StudySessionRepository;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.dto.StudySessionRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.StudySessionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudySessionServiceImplTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private StudySessionMapper studySessionMapper;

    @InjectMocks
    private StudySessionServiceImpl studySessionService;

    private StudySession studySession;

    private List<StudySession> studySessions;

    @BeforeEach
    void init() {
        String id = "1";
        String categoryId = "2";
        String name = "Solar System";
        this.studySession = new StudySession(id, categoryId, name);
        this.studySessions = List.of(studySession);
    }

    @Test
    void testFindAll() {
        when(studySessionRepository.findAll()).thenReturn(studySessions);
        assertEquals(studySessions, studySessionService.findAll());
        verify(studySessionRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(studySessionRepository.findById("1")).thenReturn(Optional.of(studySession));
        assertEquals(studySession, studySessionService.findById("1"));
        verify(studySessionRepository, times(1)).findById("1");
    }

    @Test
    void findByIdThrowsExceptionWhenStudySessionDoesNotExist() {
        when(studySessionRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> studySessionService.findById("1"));
        verify(studySessionRepository, times(1)).findById("1");
    }

    @Test
    void throwsExceptionWhenCreatingStudySessionWithNonExistentCategory() {
        doThrow(new NotFoundException("Cannot find category with id = 2")).when(categoryService).assertExistsById("2");
        StudySessionRequest request = new StudySessionRequest("2", "Solar System");

        assertThrows(NotFoundException.class, () -> studySessionService.createStudySession(request));
        verify(categoryService, times(1)).assertExistsById("2");
    }

    @Test
    void testCreateStudySession() {
        StudySessionRequest request = new StudySessionRequest("2", "Solar System");

        when(studySessionMapper.studySessionFrom(request)).thenReturn(studySession);
        when(studySessionRepository.save(studySession)).thenReturn(studySession);

        assertEquals(studySession, studySessionService.createStudySession(request));
        verify(categoryService, times(1)).assertExistsById("2");
        verify(studySessionMapper, times(1)).studySessionFrom(request);
        verify(studySessionRepository, times(1)).save(studySession);
    }

    @Test
    void testSave() {
        when(studySessionRepository.save(studySession)).thenReturn(studySession);

        assertEquals(studySession, studySessionService.save(studySession));
        verify(studySessionRepository, times(1)).save(studySession);
    }

    @Test
    void testExistsById() {
        when(studySessionRepository.existsById("1")).thenReturn(true);
        when(studySessionRepository.existsById("2")).thenReturn(false);

        assertTrue(studySessionService.existsById("1"));
        assertFalse(studySessionService.existsById("2"));
        verify(studySessionRepository, times(1)).existsById("1");
        verify(studySessionRepository, times(1)).existsById("2");
    }

    @Test
    void deletesExistentStudySession() {
        when(studySessionRepository.existsById("1")).thenReturn(true);

        studySessionService.deleteById("1");
        verify(studySessionRepository, times(1)).deleteById("1");
    }

    @Test
    void throwsExceptionWhenDeletingNonExistentStudySession() {
        when(studySessionRepository.existsById("1")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> studySessionService.deleteById("1"));
        verify(studySessionRepository, times(1)).existsById("1");
    }

    @Test
    void assertExistsByIdShouldThrowExceptionWhenStudySessionDoesNotExistById() {
        when(studySessionRepository.existsById("1")).thenReturn(false);
        assertThrows(NotFoundException.class, () -> studySessionService.assertExistsById("1"));
    }

    @Test
    void assertExistsByIdShouldNotThrowExceptionWhenStudySessionExistsById() {
        when(studySessionRepository.existsById("1")).thenReturn(true);
        assertDoesNotThrow(() -> studySessionService.assertExistsById("1"));
    }

}
