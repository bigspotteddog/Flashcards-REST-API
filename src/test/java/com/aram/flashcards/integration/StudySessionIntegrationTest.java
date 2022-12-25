package com.aram.flashcards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudySessionIntegrationTest {

    @Value("${spring.servlet.path.study-sessions}")
    private String path;

    @Autowired
    WebTestClient client;

    @Test
    void findsAll() {
        String expectedResponseBody = """
                [
                    {
                        "id":"1",
                        "categoryId":"1",
                        "name":"Guitar chords"
                    },
                    {
                        "id":"2",
                        "categoryId":"2",
                        "name":"Stock exchange"
                    }   
                ]
                """;

        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedResponseBody);
    }

    @Test
    void findsById() {
        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Guitar chords\"}");
    }

    @Test
    void returnsNotFoundWhenFindingNonExistentStudySessionById() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find study session with id = 3\"}");
    }

    @Test
    void createsStudySession() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"1\", \"name\":\"Classic music authors\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.categoryId").isEqualTo("1")
                    .jsonPath("$.name").isEqualTo("Classic music authors");
    }

    @Test
    void returnsNotFoundWhenCreatingStudySessionWithNonExistentCategory() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"3\", \"name\":\"Classic music authors\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find category with id = 3\"}");
    }

    @Test
    void returnsBadRequestWhenCreatingStudySessionWithEmptyRequestBody() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updatesExistentStudySession() {
        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Guitar chords\"}");

        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Classic music\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Classic music\"}");
    }

    @Test
    void updatesNonExistentStudySession() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"categoryId\":\"1\", \"name\":\"Classic music\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json("{\"id\":\"3\", \"categoryId\":\"1\", \"name\":\"Classic music\"}");

        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"3\", \"categoryId\":\"1\", \"name\":\"Classic music\"}");
    }

    @Test
    void returnsNotFoundWhenUpdatingStudySessionWithNonExistentCategory() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"3\", \"name\":\"Classic music\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find category with id = 3\"}");
    }

    @Test
    void returnsBadRequestWhenUpdatingStudySessionWithEmptyRequestBody() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deletesExistentStudySessionById() {
        client.get().uri(path + "/1")
              .accept(APPLICATION_JSON)
              .exchange()
              .expectStatus().isOk()
              .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Guitar chords\"}");

        client.delete().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find study session with id = 1\"}");
    }

    @Test
    void returnsNotFoundWhenDeletingNonExistentStudySession() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        client.delete().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find study session with id = 3\"}");
    }

}