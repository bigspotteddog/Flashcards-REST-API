package com.aram.flashcards.service.mapper;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.dto.StudySessionRequest;

public interface StudySessionMapper {

    StudySession studySessionFrom(StudySessionRequest request);

}
