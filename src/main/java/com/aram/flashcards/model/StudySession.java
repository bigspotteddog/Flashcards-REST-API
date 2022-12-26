package com.aram.flashcards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public class StudySession {

    @Id
    @NotBlank(message = "id is required")
    private final String id;

    @NotBlank(message = "category id is required")
    private final String categoryId;

    @NotBlank(message = "name is required")
    private final String name;

}