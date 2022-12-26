package com.aram.flashcards.service.impl;

import com.aram.flashcards.service.exception.BadRequestException;

abstract class ValidatingService {

    void assertNotNull(Object object) {
        if (object == null) {
            throw new BadRequestException("Cannot process null object");
        }
    }

}
