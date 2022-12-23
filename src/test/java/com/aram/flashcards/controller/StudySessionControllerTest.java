package com.aram.flashcards.controller;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.StudySessionRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudySessionController.class)
public class StudySessionControllerTest extends WebLayerTest {

    @Value("${spring.servlet.path.study-sessions}")
    private String studySessionsPath;

    @MockBean
    private StudySessionService studySessionService;

    @Autowired
    MockMvc mockMvc;

    private StudySession studySession;

    @BeforeEach
    void init() {
        this.studySession = new StudySession(
                "1",
                "2",
                "Solar System"
        );
    }

    @Test
    void findsAllStudySessions() throws Exception {
        when(studySessionService.findAll()).thenReturn(Set.of(studySession));

        mockMvc.perform(get(studySessionsPath)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(Set.of(studySession))));
    }

    @Test
    void findsStudySessionById() throws Exception {
        when(studySessionService.findById("1")).thenReturn(studySession);

        mockMvc.perform(get(studySessionsPath + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(studySession)));
    }

    @Test
    void returnsNotFoundWhenStudySessionDoesNotExistById() throws Exception {
        when(studySessionService.findById("1"))
                .thenThrow(new NotFoundException("Cannot find study session with id = 1"));

        mockMvc.perform(get(studySessionsPath + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Cannot find study session with id = 1\"}"));
    }

    @Test
    void createsStudySessionWhenStudySessionDoesNotExist() throws Exception {
        StudySessionRequest request = new StudySessionRequest("2", "Solar System");
        when(studySessionService.createStudySession(request)).thenReturn(studySession);

        mockMvc.perform(post(studySessionsPath)
                .contentType(APPLICATION_JSON)
                .content(serialize(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(serialize(studySession)));
    }

    @Test
    void returnsAlreadyExistsWhenStudySessionExistsByName() throws Exception {
        StudySessionRequest request = new StudySessionRequest("2", "Solar System");
        when(studySessionService.createStudySession(request))
                .thenThrow(new ConflictException("Study session with name = Solar System already exists"));

        mockMvc.perform(post(studySessionsPath)
                .contentType(APPLICATION_JSON)
                .content(serialize(request)))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"message\":\"Study session with name = Solar System already exists\"}"));
    }
    @Test
    void returnsBadRequestWhenRequestBodyToCreateStudySessionIsEmpty() throws Exception {
        mockMvc.perform(put(studySessionsPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundWhenCreatingStudySessionWithNonExistentCategory() throws Exception {
        StudySessionRequest request = new StudySessionRequest("2", "Solar System");
        when(studySessionService.createStudySession(request))
                .thenThrow(new NotFoundException("Category with id = 2 does not exist"));

        mockMvc.perform(post(studySessionsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Category with id = 2 does not exist\"}"));
    }

    @Test
    void updatesStudySessionWhenStudySessionDoesNotExist() throws Exception {
        StudySession studySession = new StudySession("1", "2", "Solar System");
        when(studySessionService.existsById("1")).thenReturn(false);
        when(studySessionService.save(studySession)).thenReturn(studySession);

        mockMvc.perform(put(studySessionsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(studySession)))
                .andExpect(status().isCreated())
                .andExpect(content().json(serialize(studySession)));
    }

    @Test
    void updatesStudySessionWhenStudySessionExists() throws Exception {
        StudySession studySession = new StudySession("1", "2", "Solar System");
        when(studySessionService.existsById("1")).thenReturn(true);
        when(studySessionService.save(studySession)).thenReturn(studySession);

        mockMvc.perform(put(studySessionsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(studySession)))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(studySession)));
    }

    @Test
    void returnsBadRequestWhenRequestBodyToUpdateStudySessionIsEmpty() throws Exception {
        mockMvc.perform(put(studySessionsPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsConflictWhenUpdatingStudySessionWithExistentName() throws Exception {
        StudySession studySession = new StudySession("1", "2", "Solar System");
        when(studySessionService.existsById("1")).thenReturn(false);
        when(studySessionService.save(studySession))
                .thenThrow(new ConflictException("Study session with name = Solar System already exists"));

        mockMvc.perform(put(studySessionsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(studySession)))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"message\":\"Study session with name = Solar System already exists\"}"));
    }

    @Test
    void returnsNotFoundWhenUpdatingStudySessionWithNonExistentCategory() throws Exception {
        StudySession studySession = new StudySession("1", "2", "Solar System");
        when(studySessionService.existsById("1")).thenReturn(false);
        when(studySessionService.save(studySession))
                .thenThrow(new NotFoundException("Category with id = 2 does not exist"));

        mockMvc.perform(put(studySessionsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(studySession)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Category with id = 2 does not exist\"}"));
    }

    @Test
    void deletesById() throws Exception {
        mockMvc.perform(delete(studySessionsPath + "/1"))
                .andExpect(status().isNoContent());

        verify(studySessionService, times(1)).deleteById("1");
    }

}