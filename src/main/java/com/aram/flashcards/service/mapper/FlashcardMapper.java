package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.service.dto.FlashcardRequest;

public interface FlashcardMapper {

    Flashcard flashcardFrom(FlashcardRequest request);

}
