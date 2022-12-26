package com.aram.flashcards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryIntegrationTest {

    @Value("${spring.servlet.path.categories}")
    private String path;

    @Autowired
    private WebTestClient client;

    @Test
    void findsAllCategoriesInAlphabeticalOrder() {
        String expectedResponseBody = """
                [
                    {
                        "id":"2",
                        "name":"Finance"
                    },
                    {
                        "id":"1",
                        "name":"Music"
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
    void findsExistentCategoryById() {
        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{'id':'1', 'name':'Music'}");
    }

    @Test
    void returnsNotFoundWhenFindingNonExistentCategoryById() {
        client.get().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{'message':'Cannot find category with id = 3'}");
    }

    @Test
    void createsCategory() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"Sports\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.name").isEqualTo("Sports");
    }

    @Test
    void returnsConflictWhenCreatingCategoryWithDuplicateName() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"Music\"}")
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody().json("{\"message\":\"Category with name = Music already exists\"}");
    }

    @Test
    void returnsBadRequestWhenCreatingCategoryWithEmptyRequestBody() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenCreatingCategoryWithEmptyName() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"name is required\"]}");
    }

    @Test
    void updatesExistentCategory() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"name\":\"Jazz Music\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Jazz Music\"}");
    }

    @Test
    void updatesNonExistentCategory() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"name\":\"Sports\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json("{\"id\":\"3\", \"name\":\"Sports\"}");
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenUpdatingCategoryWithEmptyId() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"\", \"name\":\"Sports\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"id is required\"]}");
    }

    @Test
    void returnsBadRequestWithErrorMessageWhenUpdatingCategoryWithEmptyName() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"name\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json("{\"errors\":[\"name is required\"]}");
    }

    @Test
    void returnsConflictWhenUpdatingCategoryWithDuplicateName() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"name\":\"Finance\"}")
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody().json("{\"message\":\"Category with name = Finance already exists\"}");
    }

    @Test
    void returnsBadRequestWhenUpdatingCategoryWithEmptyRequestBody() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deletesExistentCategoryById() {
        client.delete().uri(path + "/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void returnsNotFoundWhenDeletingNonExistentCategory() {
        client.delete().uri(path + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find category with id = 3\"}");
    }

}