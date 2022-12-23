package com.aram.flashcards.service.impl;

import com.aram.flashcards.service.exception.BadRequestException;

abstract class ValidatingService {

    private static final String ERROR_MESSAGE_ABSENT_ID = "id cannot be empty or null";

    void assertNonNull(Object object) {
        if (object == null) {
            throw new BadRequestException("Cannot process null object");
        }
    }

    void validateId(String tested) {
        if (tested == null || tested.trim().isEmpty()) {
            throw new BadRequestException(ERROR_MESSAGE_ABSENT_ID);
        }
    }

}
