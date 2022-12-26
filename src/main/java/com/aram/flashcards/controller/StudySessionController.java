package com.aram.flashcards.controller;

import com.aram.flashcards.model.StudySession;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.StudySessionRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/study-sessions")
public class StudySessionController implements ResponseHandler {

    private final StudySessionService studySessionService;

    @Autowired
    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @GetMapping
    public ResponseEntity<Iterable<StudySession>> findAll() {
        return ok(studySessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySession> findById(@PathVariable String id) {
        return ok(studySessionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudySession> createStudySession(@Valid @RequestBody StudySessionRequest request) {
        return created(studySessionService.createStudySession(request));
    }

    @PutMapping
    public ResponseEntity<StudySession> update(@Valid @RequestBody StudySession studySession) {
        return existsById(studySession.getId()) ? ok(save(studySession)) : created(save(studySession));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudySession> deleteById(@PathVariable String id) {
        studySessionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean existsById(String id) {
        return studySessionService.existsById(id);
    }

    private StudySession save(StudySession studySession) {
        return studySessionService.save(studySession);
    }

}
