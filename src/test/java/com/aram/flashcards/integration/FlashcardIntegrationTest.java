package com.aram.flashcards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Sql({"/test-data.sql"})
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FlashcardIntegrationTest {

    @Value("${spring.servlet.path.flashcards}")
    private String path;

    @Autowired
    WebTestClient client;

    @Test
    void loadsTestData() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[?(@.question == 'What kind of star is the sun?')]").exists();
    }

    @Test
    void loadsInitialFlashcards() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[?(@.question == 'What is the grid technique?')]").exists()
                    .jsonPath("$[?(@.question == 'What is scribbling?')]").exists()
                    .jsonPath("$[?(@.question == 'What is stop motion animation?')]").exists()
                    .jsonPath("$[?(@.question == 'What is rotoscope animation?')]").exists()
                    .jsonPath("$[?(@.question == 'What are the types of chemical reactions?')]").exists()
                    .jsonPath("$[?(@.question == 'What is a covalent bond?')]").exists()
                    .jsonPath("$[?(@.question == 'What are the four types of nuclear reactions?')]").exists()
                    .jsonPath("$[?(@.question == 'What is nuclear fusion?')]").exists()
                    .jsonPath("$[?(@.question == 'What are thriller movies?')]").exists()
                    .jsonPath("$[?(@.question == 'What are comedy movies?')]").exists()
                    .jsonPath("$[?(@.question == 'Who directed Inception?')]").exists()
                    .jsonPath("$[?(@.question == 'Who directed The Shining?')]").exists()
                    .jsonPath("$[?(@.question == 'Who composed the ninth symphony?')]").exists()
                    .jsonPath("$[?(@.question == 'Who composed the Jupiter symphony?')]").exists()
                    .jsonPath("$[?(@.question == 'What is the tonic?')]").exists()
                    .jsonPath("$[?(@.question == 'What is a chord progression?')]").exists()
                    .jsonPath("$[?(@.question == 'What is the S of the SOLID principles?')]").exists()
                    .jsonPath("$[?(@.question == 'What are the four pillars of OOP?')]").exists()
                    .jsonPath("$[?(@.question == 'What is a higher order function?')]").exists()
                    .jsonPath("$[?(@.question == 'What is function composition?')]").exists();
    }

    @Test
    void findsById() {
        String expectedResponseBody = """
                {
                    "id":"1",
                    "studySessionId":"1",
                    "question":"What kind of star is the sun?",
                    "answer":"Yellow dwarf"
                }
                """;

        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedResponseBody);
    }

    @Test
    void returnsNotFoundWhenFindingNonExistentFlashcardById() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find flashcard with id = 3\"}");
    }

    @Test
    void createsFlashcard() {
        String requestBody = """
                {
                    "studySessionId":"1",
                    "question":"What types of guitars do exist?",
                    "answer":"Acoustic and electric"
                }
                """;

        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.studySessionId").isEqualTo("1")
                    .jsonPath("$.question").isEqualTo("What types of guitars do exist?")
                    .jsonPath("$.answer").isEqualTo("Acoustic and electric");
    }

    @Test
    void returnsNotFoundWhenCreatingFlashcardWithNonExistentStudySession() {
        String requestBody = """
                {
                    "studySessionId":"3",
                    "question":"What is tennis?",
                    "answer":"A sport that is played with a ball and two rackets"
                }
                """;

        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find study session with id = 3\"}");
    }

    @Test
    void returnsBadRequestWhenCreatingFlashcardWithEmptyRequestBody() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void returnsBadRequestWithErrorMessagesWhenCreatingFlashcardWithEmptyValues() {
        String requestBody = """
                {
                    "studySessionId":"",
                    "question":"",
                    "answer":""
                }
                """;

        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                        {
                            "errors":[
                                "study session id is required",
                                "question is required",
                                "answer is required"
                            ]
                        }
                        """);
    }

    @Test
    void updatesExistentFlashcard() {
        String requestBody = """
                {
                    "id":"1",
                    "studySessionId":"1",
                    "question":"What is an electric guitar?",
                    "answer":"A guitar that can be plugged to an amplifier"
                }
                """;

        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(requestBody);
    }

    @Test
    void updatesNonExistentFlashcard() {
        String requestBody = """
                {
                    "id":"3",
                    "studySessionId":"1",
                    "question":"What is an electric guitar?",
                    "answer":"A guitar that can be plugged to an amplifier"
                }
                """;

        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json(requestBody);
    }

    @Test
    void returnsBadRequestWithErrorMessagesWhenUpdatingFlashcardWithEmptyValues() {
        String requestBody = """
                {
                    "id":"",
                    "studySessionId":"",
                    "question":"",
                    "answer":""
                }
                """;

        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("""
                        {
                            "errors":[
                                "id is required",
                                "study session id is required",
                                "question is required",
                                "answer is required"
                            ]
                        }
                        """);
    }

}