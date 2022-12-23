package com.aram.flashcards.model;

import com.aram.flashcards.model.exception.InvalidEntityDataException;

public interface ValidatingEntity {

    default void validateEntityString(String tested, String message) {
        if (tested == null || tested.trim().isEmpty()) {
            throw new InvalidEntityDataException(message);
        }
    }

}
