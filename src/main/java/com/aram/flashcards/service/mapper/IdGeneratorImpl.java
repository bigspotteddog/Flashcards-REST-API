package com.aram.flashcards.service.mapper;

import com.aram.flashcards.service.IdGenerator;
import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
class IdGeneratorImpl implements IdGenerator {

    @Override
    public String generateId() {
        return randomUUID().toString();
    }

}
