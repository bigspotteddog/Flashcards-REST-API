package com.aram.flashcards.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class StudySessionRequest {

    private final String categoryId;
    private final String name;

}
