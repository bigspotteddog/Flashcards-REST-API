package com.aram.flashcards.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class FlashcardRequest {

    @NotBlank(message = "study session id is required")
    private final String studySessionId;

    @NotBlank(message = "question is required")
    private final String question;

    @NotBlank(message = "answer is required")
    private final String answer;

}
