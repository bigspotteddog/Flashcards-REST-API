package com.aram.flashcards.data;

import com.aram.flashcards.service.FlashcardService;
import com.aram.flashcards.service.StudySessionService;
import com.aram.flashcards.service.dto.FlashcardRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class FlashcardsLoader {

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private FlashcardService flashcardService;

    void loadFlashcards() {
        flashcardRequests().forEach(flashcardService::createFlashcard);
    }

    private List<FlashcardRequest> flashcardRequests() {

        return List.of(

            new FlashcardRequest(
                    idFromStudySessionWithName("Drawing techniques"),
                    "What is the grid technique?",
                    "A technique to draw with the help of a grid"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Drawing techniques"),
                    "What is scribbling?",
                    "The action of drawing something carelessly"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Animation techniques"),
                    "What is stop motion animation?",
                    "A technique in which objects are physically manipulated in small increments"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Animation techniques"),
                    "What is rotoscope animation?",
                    "A technique to trace over live-action motion picture footage frame by frame"
            ),

            new FlashcardRequest(
                idFromStudySessionWithName("Chemical reactions"),
                "What are the types of chemical reactions?",
                "Combination, decomposition, single-replacement, double-replacement, and combustion"
            ),

            new FlashcardRequest(
                idFromStudySessionWithName("Chemical reactions"),
                "What is a covalent bond?",
                "The linkage that results from the sharing of an electron pair between two atoms"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Nuclear chemistry"),
                    "What are the four types of nuclear reactions?",
                    "Fission, fusion, nuclear decay and transmutation"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Nuclear chemistry"),
                    "What is nuclear fusion?",
                    "The process by which two light atomic nuclei combine to form a single heavier one"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Cinema genres"),
                    "What are thriller movies?",
                    "A film genre that evokes excitement and suspense in the audience"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Cinema genres"),
                    "What are comedy movies?",
                    "A category of film which emphasizes humor"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Famous directors"),
                    "Who directed Inception?",
                    "Christopher Nolan"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Famous directors"),
                    "Who directed The Shining?",
                    "Stanley Kubrick"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Classic music"),
                    "Who composed the ninth symphony?",
                    "Beethoven"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Classic music"),
                    "Who composed the Jupiter symphony?",
                    "Mozart"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Chords"),
                    "What is the tonic?",
                    "The first note on a musical scale"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Chords"),
                    "What is a chord progression?",
                    "A succession of chords"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Object oriented programming"),
                    "What is the S of the SOLID principles?",
                    "Single responsibility principle"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Object oriented programming"),
                    "What are the four pillars of OOP?",
                    "Abstraction, inheritance, encapsulation and polymorphism"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Functional programming"),
                    "What is a higher order function?",
                    "A function that takes a function as an argument, or returns a function"
            ),

            new FlashcardRequest(
                    idFromStudySessionWithName("Functional programming"),
                    "What is function composition?",
                    "Combining simple functions to build more complicated ones"
            )

        );

    }

    private String idFromStudySessionWithName(String name) {
        return studySessionService.idFromStudySessionWithName(name);
    }

}
