package com.aram.flashcards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor(force = true, access = PRIVATE)
public class Category implements ValidatingEntity {

    @Id
    private final String id;
    private final String name;

    public Category(String id, String name) {
        validateEntityString(id, "id cannot be null or empty");
        validateEntityString(name, "name cannot be null or empty");
        this.id = id;
        this.name = name;
    }

}