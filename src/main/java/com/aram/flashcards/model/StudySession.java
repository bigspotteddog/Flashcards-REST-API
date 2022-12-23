package com.aram.flashcards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor(force = true, access = PRIVATE)
public class StudySession implements ValidatingEntity {

    @Id
    private final String id;
    private final String categoryId;
    private final String name;
    
    public StudySession(String id, String categoryId, String name) {
        validateEntityString(id, "id cannot be empty or null");
        validateEntityString(categoryId, "categoryId cannot be empty or null");
        validateEntityString(name, "name cannot be empty or null");
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

}
