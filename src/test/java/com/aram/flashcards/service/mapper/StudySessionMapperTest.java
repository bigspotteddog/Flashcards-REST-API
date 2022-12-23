package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.StudySessionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudySessionMapperTest {

    @Mock
    IdGenerator idGenerator;

    @InjectMocks
    private StudySessionMapperImpl studySessionMapper;

    @Test
    void testStudySessionFrom() {
        String categoryId = "2";
        StudySessionRequest request = new StudySessionRequest(
                categoryId,
                "Planets in the Solar System"
        );

        when(idGenerator.generateId()).thenReturn("1");
        StudySession studySession = studySessionMapper.studySessionFrom(request);

        assertEquals("1", studySession.getId());
        assertEquals("2", studySession.getCategoryId());
        assertEquals("Planets in the Solar System", studySession.getName());
        verify(idGenerator, times(1)).generateId();
    }

}