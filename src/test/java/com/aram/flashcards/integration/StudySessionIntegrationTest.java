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
public class StudySessionIntegrationTest {

    @Value("${spring.servlet.path.study-sessions}")
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
                    .jsonPath("$[?(@.name == 'Solar system')]").exists();
    }

    @Test
    void loadsInitialStudySessions() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[?(@.name == 'Drawing techniques')]").exists()
                    .jsonPath("$[?(@.name == 'Animation techniques')]").exists()
                    .jsonPath("$[?(@.name == 'Chemical reactions')]").exists()
                    .jsonPath("$[?(@.name == 'Nuclear chemistry')]").exists()
                    .jsonPath("$[?(@.name == 'Cinema genres')]").exists()
                    .jsonPath("$[?(@.name == 'Famous directors')]").exists()
                    .jsonPath("$[?(@.name == 'Classic music')]").exists()
                    .jsonPath("$[?(@.name == 'Chords')]").exists()
                    .jsonPath("$[?(@.name == 'Object oriented programming')]").exists()
                    .jsonPath("$[?(@.name == 'Functional programming')]").exists();
    }

    @Test
    void findsById() {
        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Solar system\"}");
    }

    @Test
    void returnsNotFoundWhenFindingNonExistentStudySessionById() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"error\":\"Cannot find study session with id = 3\"}");
    }

    @Test
    void findsAllByCategoryId() {
        client.get().uri(path + "/details?categoryId=1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .json("[{'id':'1', 'categoryId':'1', 'name':'Solar system'}]");
    }

    @Test
    void returnsNotFoundWhenFindingAllStudySessionsFromNonExistentCategory() {
        client.get().uri(path + "/details?categoryId=3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{'error':'Cannot find category with id = 3'}");
    }

    @Test
    void createsStudySession() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"1\", \"name\":\"Types of stars\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.categoryId").isEqualTo("1")
                    .jsonPath("$.name").isEqualTo("Types of stars");
    }

    @Test
    void returnsNotFoundWhenCreatingStudySessionWithNonExistentCategory() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"3\", \"name\":\"Classic music authors\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"error\":\"Cannot find category with id = 3\"}");
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
    void returnsBadRequestWithErrorMessageWhenCreatingStudySessionWithEmptyCategoryId() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"\", \"name\":\"Water sports\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"category id is required\"]}");
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenCreatingStudySessionWithEmptyName() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"categoryId\":\"3\", \"name\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"name is required\"]}");
    }

    @Test
    void updatesExistentStudySession() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Types of stars\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"Types of stars\"}");
    }

    @Test
    void updatesNonExistentStudySession() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"categoryId\":\"1\", \"name\":\"Types of stars\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json("{\"id\":\"3\", \"categoryId\":\"1\", \"name\":\"Types of stars\"}");
    }

    @Test
    void returnsNotFoundWhenUpdatingStudySessionWithNonExistentCategory() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"3\", \"name\":\"Classic music\"}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"error\":\"Cannot find category with id = 3\"}");
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
    void returnsBadRequestWithErrorMessageWhenUpdatingStudySessionWithEmptyId() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"\", \"categoryId\":\"1\", \"name\":\"Types of planets\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"id is required\"]}");
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenUpdatingStudySessionWithEmptyName() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"1\", \"name\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"name is required\"]}");
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenUpdatingStudySessionWithEmptyCategoryId() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"categoryId\":\"\", \"name\":\"Types of planets\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"category id is required\"]}");
    }

    @Test
    void deletesExistentStudySessionById() {
        client.delete().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void returnsNotFoundWhenDeletingNonExistentStudySession() {
        client.delete().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"error\":\"Cannot find study session with id = 3\"}");
    }

}