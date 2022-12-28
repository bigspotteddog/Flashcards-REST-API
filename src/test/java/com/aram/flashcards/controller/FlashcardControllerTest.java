package com.aram.flashcards.controller;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.FlashcardService;
import com.aram.flashcards.service.dto.FlashcardRequest;
import com.aram.flashcards.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlashcardController.class)
public class FlashcardControllerTest extends WebLayerTest {

    @Value("${spring.servlet.path.flashcards}")
    private String flashcardsPath;

    @MockBean
    FlashcardService flashcardService;

    @Autowired
    MockMvc mockMvc;

    private Flashcard flashcard;

    @BeforeEach
    void init() {
        this.flashcard = new Flashcard(
                "1",
                "2",
                "What color is the sky?",
                "Blue"
        );
    }

    @Test
    void findsAllFlashcards() throws Exception {
        when(flashcardService.findAll()).thenReturn(Set.of(flashcard));

        mockMvc.perform(get(flashcardsPath))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(Set.of(flashcard))));
    }

    @Test
    void findsCategoryByIdWhenFlashcardExistsById() throws Exception {
        when(flashcardService.findById("1")).thenReturn(flashcard);

        mockMvc.perform(get(flashcardsPath + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize((flashcard))));
    }

    @Test
    void returnsNotFoundWhenFlashcardDoesNotExistById() throws Exception {
        when(flashcardService.findById("1"))
                .thenThrow(new NotFoundException("Flashcard with id = 1 does not exist"));

        mockMvc.perform(get(flashcardsPath + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(content().json("{'error':'Flashcard with id = 1 does not exist'}"));
    }

    @Test
    void findsAllByStudySessionIdWhenStudySessionExists() throws Exception {
        when(flashcardService.findAllByStudySessionId("1"))
                .thenReturn(Set.of(flashcard));

        mockMvc.perform(get(flashcardsPath + "/details?studySessionId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(Set.of(flashcard))));
    }

    @Test
    void createsFlashcard() throws Exception {
        FlashcardRequest request = new FlashcardRequest(
                "1",
                "What color is the sky?",
                "Blue"
        );
        when(flashcardService.createFlashcard(request)).thenReturn(flashcard);

        mockMvc.perform(post(flashcardsPath)
                .contentType(APPLICATION_JSON)
                .content(serialize(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(serialize(flashcard)));
    }

    @Test
    void returnsBadRequestWhenRequestBodyToCreateFlashcardIsEmpty() throws Exception {
        mockMvc.perform(post(flashcardsPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundWhenCreatingFlashcardWithNonExistentStudySession() throws Exception {
        FlashcardRequest request = new FlashcardRequest(
                "1",
                "What color is the sky?",
                "Blue"
        );
        when(flashcardService.createFlashcard(request))
                .thenThrow(new NotFoundException("Study session with id = 1 does not exist"));

        mockMvc.perform(post(flashcardsPath)
                .contentType(APPLICATION_JSON)
                .content(serialize(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'error':'Study session with id = 1 does not exist'}"));
    }

    @Test
    void updatesFlashcardWhenFlashcardExistsById() throws Exception {
        when(flashcardService.existsById(flashcard.getId())).thenReturn(true);
        when(flashcardService.save(flashcard)).thenReturn(flashcard);

        mockMvc.perform(put(flashcardsPath)
                .contentType(APPLICATION_JSON)
                .content(serialize(flashcard)))
                .andExpect(status().isOk())
                .andExpect(content().json(serialize(flashcard)));
    }

    @Test
    void updatesFlashcardWhenFlashcardDoesNotExistById() throws Exception {
        when(flashcardService.existsById(flashcard.getId())).thenReturn(false);
        when(flashcardService.save(flashcard)).thenReturn(flashcard);

        mockMvc.perform(put(flashcardsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(flashcard)))
                .andExpect(status().isCreated())
                .andExpect(content().json(serialize(flashcard)));
    }

    @Test
    void returnsBadRequestWhenRequestBodyToUpdateFlashcardIsEmpty() throws Exception {
        mockMvc.perform(put(flashcardsPath)
                .contentType(APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundWhenUpdatingFlashcardWithNonExistentStudySession() throws Exception {
        Flashcard flashcard = new Flashcard(
                "1",
                "2",
                "What color is the sky?",
                "Blue"
        );
        when(flashcardService.existsById("1")).thenReturn(false);
        when(flashcardService.save(flashcard))
                .thenThrow(new NotFoundException("Study session with id = 2 does not exist"));

        mockMvc.perform(put(flashcardsPath)
                        .contentType(APPLICATION_JSON)
                        .content(serialize(flashcard)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'error':'Study session with id = 2 does not exist'}"));
    }

    @Test
    void deletesById() throws Exception {
        mockMvc.perform(delete(flashcardsPath + "/1"))
                .andExpect(status().isNoContent());

        verify(flashcardService, times(1)).deleteById("1");
    }

}
