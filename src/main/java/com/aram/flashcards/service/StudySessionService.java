package com.aram.flashcards.service;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.dto.StudySessionRequest;

public interface StudySessionService {

    Iterable<StudySession> findAll();

    StudySession findById(String id);

    StudySession createStudySession(StudySessionRequest request);

    StudySession save(StudySession studySession);

    boolean existsById(String id);

    void deleteById(String id);

    void assertExistsById(String id);

    String idFromStudySessionWithName(String name);

}
