package com.aram.flashcards.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class StudySessionRequest {

    @NotBlank(message = "category id is required")
    private final String categoryId;

    @NotBlank(message = "name is required")
    private final String name;

}