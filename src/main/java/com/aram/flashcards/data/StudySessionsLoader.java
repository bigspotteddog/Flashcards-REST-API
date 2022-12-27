package com.aram.flashcards.data;

import com.aram.flashcards.service.CategoryService;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.StudySessionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class StudySessionsLoader {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StudySessionService studySessionService;

    void loadStudySessions() {
        studySessions().forEach(studySessionService::createStudySession);
    }

    private List<StudySessionRequest> studySessions() {
        return List.of(
                new StudySessionRequest(idFromCategoryWithName("Art"), "Drawing techniques"),
                new StudySessionRequest(idFromCategoryWithName("Art"), "Animation techniques"),
                new StudySessionRequest(idFromCategoryWithName("Chemistry"), "Chemical reactions"),
                new StudySessionRequest(idFromCategoryWithName("Chemistry"), "Nuclear chemistry"),
                new StudySessionRequest(idFromCategoryWithName("Cinema"), "Cinema genres"),
                new StudySessionRequest(idFromCategoryWithName("Cinema"), "Famous directors"),
                new StudySessionRequest(idFromCategoryWithName("Music"), "Classic music"),
                new StudySessionRequest(idFromCategoryWithName("Music"), "Chords"),
                new StudySessionRequest(idFromCategoryWithName("Web development"), "Object oriented programming"),
                new StudySessionRequest(idFromCategoryWithName("Web development"), "Functional programming")
        );
    }

    private String idFromCategoryWithName(String categoryName) {
        return categoryService.idFromCategoryWithName(categoryName);
    }

}