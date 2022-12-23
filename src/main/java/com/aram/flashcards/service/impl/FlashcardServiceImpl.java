package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.Flashcard;
import com.aram.flashcards.repository.FlashcardRepository;
import com.aram.flashcards.service.FlashcardService;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.FlashcardRequest;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.FlashcardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
class FlashcardServiceImpl extends ValidatingService implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final StudySessionService studySessionService;
    private final FlashcardMapper mapper;

    @Autowired
    FlashcardServiceImpl(FlashcardRepository flashcardRepository,
                         StudySessionService studySessionService,
                         FlashcardMapper mapper) {
        this.flashcardRepository = flashcardRepository;
        this.studySessionService = studySessionService;
        this.mapper = mapper;
    }

    @Override
    public Iterable<Flashcard> findAll() {
        return flashcardRepository.findAll();
    }

    @Override
    public Flashcard findById(String id) {
        validateId(id);
        return flashcardRepository.findById(id).orElseThrow(
                () -> new NotFoundException(format("Cannot find flashcard with id = %s", id)));
    }

    @Override
    public Flashcard createFlashcard(FlashcardRequest request) {
        validate(request);
        Flashcard flashcard = flashcardFrom(request);
        return flashcardRepository.save(flashcard);
    }

    @Override
    public boolean existsById(String id) {
        validateId(id);
        return flashcardRepository.existsById(id);
    }

    @Override
    public Flashcard save(Flashcard flashcard) {
        validate(flashcard);
        return flashcardRepository.save(flashcard);
    }

    @Override
    public void deleteById(String id) {
        validateId(id);
        flashcardRepository.deleteById(id);
    }

    private void validate(FlashcardRequest request) {
        assertNonNull(request);
        studySessionService.assertExistsById(request.getStudySessionId());
    }

    private void validate(Flashcard flashcard) {
        assertNonNull(flashcard);
        studySessionService.assertExistsById(flashcard.getStudySessionId());
    }

    private Flashcard flashcardFrom(FlashcardRequest request) {
        return mapper.flashcardFrom(request);
    }

}
