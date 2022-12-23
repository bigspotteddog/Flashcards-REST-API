package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.IdGenerator;
import com.aram.flashcards.service.dto.StudySessionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class StudySessionMapperImpl implements StudySessionMapper {

    private final IdGenerator idGenerator;

    @Autowired
    StudySessionMapperImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public StudySession studySessionFrom(StudySessionRequest request) {
        return new StudySession(
                idGenerator.generateId(),
                request.getCategoryId(),
                request.getName()
        );
    }

}
