package com.aram.flashcards.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class FlashcardRequest {

    private final String studySessionId;
    private final String question;
    private final String answer;

}
