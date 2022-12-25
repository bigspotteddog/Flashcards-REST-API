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
    private String categoriesPath;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findsAllCategories() {
        String expectedResponseBody = """
                [
                    {
                        "id":"1",
                        "name":"Music"
                    },
                    {
                        "id":"2",
                        "name":"Finance"
                    }
                ]
                """;

        webTestClient.get().uri(categoriesPath)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedResponseBody);
    }

    @Test
    void findsExistentCategoryById() {
        webTestClient.get().uri(categoriesPath + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{'id':'1', 'name':'Music'}");
    }

    @Test
    void returnsNotFoundWhenFindingNonExistentCategoryById() {
        webTestClient.get().uri(categoriesPath + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{'message':'Cannot find category with id = 3'}");
    }

    @Test
    void createsCategory() {
        webTestClient.get().uri(categoriesPath + "/details?name=Sports")
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isNotFound()
                        .expectBody().json("{\"message\":\"Cannot find category with name = Sports\"}");

        webTestClient.post().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"Sports\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.name").isEqualTo("Sports");

        webTestClient.get().uri(categoriesPath + "/details?name=Sports")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.name").isEqualTo("Sports");
    }

    @Test
    void returnsConflictWhenCreatingCategoryWithDuplicateName() {
        webTestClient.get().uri(categoriesPath + "/details?name=Music")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Music\"}");

        webTestClient.post().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"Music\"}")
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody().json("{\"message\":\"Category with name = Music already exists\"}");
    }

    @Test
    void returnsBadRequestWhenCreatingCategoryWithEmptyRequestBody() {
        webTestClient.post().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updatesExistentCategory() {
        webTestClient.get().uri(categoriesPath + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Music\"}");

        webTestClient.put().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"1\", \"name\":\"Jazz Music\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Jazz Music\"}");
    }

    @Test
    void updatesNonExistentCategory() {
        webTestClient.get().uri(categoriesPath + "/3")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find category with id = 3\"}");

        webTestClient.put().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"name\":\"Sports\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json("{\"id\":\"3\", \"name\":\"Sports\"}");
    }

    @Test
    void returnsConflictWhenUpdatingCategoryWithDuplicateName() {
        webTestClient.get().uri(categoriesPath + "/details?name=Finance")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"2\", \"name\":\"Finance\"}");

        webTestClient.put().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"name\":\"Finance\"}")
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody().json("{\"message\":\"Category with name = Finance already exists\"}");
    }

    @Test
    void returnsBadRequestWhenUpdatingCategoryWithEmptyRequestBody() {
        webTestClient.put().uri(categoriesPath)
                .contentType(APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deletesById() {
        webTestClient.get().uri(categoriesPath + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Music\"}");

        webTestClient.delete().uri(categoriesPath + "/1")
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(categoriesPath + "/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"message\":\"Cannot find category with id = 1\"}");
    }

}