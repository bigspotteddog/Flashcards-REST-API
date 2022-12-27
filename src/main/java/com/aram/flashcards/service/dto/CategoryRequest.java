package com.aram.flashcards.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "name is required")
    private final String name;

}
