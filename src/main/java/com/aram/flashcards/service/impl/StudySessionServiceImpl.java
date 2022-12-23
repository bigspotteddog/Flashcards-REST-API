package com.aram.flashcards.service.impl;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.repository.StudySessionRepository;
import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.StudySessionRequest;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.NotFoundException;
import com.aram.flashcards.service.mapper.StudySessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
class StudySessionServiceImpl extends ValidatingService implements StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final CategoryService categoryService;
    private final StudySessionMapper studySessionMapper;

    @Autowired
    StudySessionServiceImpl(StudySessionRepository studySessionRepository,
                            CategoryService categoryService,
                            StudySessionMapper studySessionMapper) {
        this.studySessionRepository = studySessionRepository;
        this.categoryService = categoryService;
        this.studySessionMapper = studySessionMapper;
    }

    @Override
    public Iterable<StudySession> findAll() {
        return studySessionRepository.findAll();
    }

    @Override
    public StudySession findById(String id) {
        validateId(id);
        return studySessionRepository.findById(id).orElseThrow(
                () -> new NotFoundException(format("Cannot find study session with id = %s", id)));
    }

    @Override
    public StudySession createStudySession(StudySessionRequest request) {
        validate(request);
        StudySession studySession = studySessionFrom(request);
        return studySessionRepository.save(studySession);
    }

    @Override
    public StudySession save(StudySession studySession) {
        validate(studySession);
        return studySessionRepository.save(studySession);
    }

    @Override
    public boolean existsById(String id) {
        validateId(id);
        return studySessionRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        validateId(id);
        studySessionRepository.deleteById(id);
    }

    @Override
    public void assertExistsById(String id) {
        if (!existsById(id)) {
            throw new NotFoundException(format("Cannot find study session with id = %s", id));
        }
    }

    private void validate(StudySession studySession) {
        assertNonNull(studySession);
        assertDoesNotExistByName(studySession.getName());
        categoryService.assertExistsById(studySession.getCategoryId());
    }

    private void validate(StudySessionRequest request) {
        assertNonNull(request);
        assertDoesNotExistByName(request.getName());
        categoryService.assertExistsById(request.getCategoryId());
    }

    private void assertDoesNotExistByName(String name) {
        if (studySessionRepository.existsByName(name)) {
            throw new ConflictException(format("Study session with name = %s already exists", name));
        }
    }

    private StudySession studySessionFrom(StudySessionRequest request) {
        return studySessionMapper.studySessionFrom(request);
    }

}
