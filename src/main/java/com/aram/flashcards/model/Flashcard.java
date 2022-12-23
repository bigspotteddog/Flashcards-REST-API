package com.aram.flashcards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor(force = true, access = PRIVATE)
public class Flashcard implements ValidatingEntity {

    @Id
    private final String id;
    private final String studySessionId;
    private final String question;
    private final String answer;
    
    public Flashcard(String id, String studySessionId, String question, String answer) {
        validateEntityString(id, "id cannot be null or empty");
        validateEntityString(studySessionId, "studySessionId cannot be null or empty");
        validateEntityString(question, "question cannot be null or empty");
        validateEntityString(answer, "answer cannot be null or empty");
        this.id = id;
        this.studySessionId = studySessionId;
        this.answer = answer;
        this.question = question;
    }

}