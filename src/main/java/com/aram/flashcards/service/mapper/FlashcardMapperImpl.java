package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.FlashcardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class FlashcardMapperImpl implements FlashcardMapper {

    private final IdGenerator idGenerator;

    @Autowired
    FlashcardMapperImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Flashcard flashcardFrom(FlashcardRequest request) {
        return new Flashcard(
                idGenerator.generateId(),
                request.getStudySessionId(),
                request.getQuestion(),
                request.getAnswer()
        );
    }

}
