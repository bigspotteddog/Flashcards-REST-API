package com.aram.flashcards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Sql({"/test-data.sql"})
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryIntegrationTest {

    @Value("${spring.servlet.path.categories}")
    private String path;

    @Autowired
    private WebTestClient client;

    @Test
    void loadsTestData() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[?(@.name == 'Astronomy')]").exists();
    }

    @Test
    void loadsInitialCategories() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$[?(@.name == 'Art')]").exists()
                    .jsonPath("$[?(@.name == 'Biology')]").exists()
                    .jsonPath("$[?(@.name == 'Chemistry')]").exists()
                    .jsonPath("$[?(@.name == 'Cinema')]").exists()
                    .jsonPath("$[?(@.name == 'Engineering')]").exists()
                    .jsonPath("$[?(@.name == 'Graphic design')]").exists()
                    .jsonPath("$[?(@.name == 'History')]").exists()
                    .jsonPath("$[?(@.name == 'Math')]").exists()
                    .jsonPath("$[?(@.name == 'Medicine')]").exists()
                    .jsonPath("$[?(@.name == 'Music')]").exists()
                    .jsonPath("$[?(@.name == 'Physics')]").exists()
                    .jsonPath("$[?(@.name == 'Sports')]").exists()
                    .jsonPath("$[?(@.name == 'Web development')]").exists();
    }

    @Test
    void findsAllCategoriesByAlphabeticalOrder() {
        client.get().uri(path)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.length()").isEqualTo(14)
                    .jsonPath("$[0].name").isEqualTo("Art")
                    .jsonPath("$[13].name").isEqualTo("Web development");
    }

    @Test
    void findsExistentCategoryById() {
        client.get().uri(path + "/1")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{'id':'1', 'name':'Astronomy'}");
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
    void createsCategoryWhenCategoryDoesNotExistByName() {
        client.post().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"name\":\"Tourism\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.name").isEqualTo("Tourism");
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
                .bodyValue("{\"id\":\"1\", \"name\":\"Tourism\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":\"1\", \"name\":\"Tourism\"}");
    }

    @Test
    void updatesNonExistentCategory() {
        client.put().uri(path)
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"id\":\"3\", \"name\":\"Tourism\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().json("{\"id\":\"3\", \"name\":\"Tourism\"}");
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
                .bodyValue("{\"id\":\"1\", \"name\":\"Music\"}")
                .exchange()
                .expectStatus().isEqualTo(CONFLICT.value())
                .expectBody().json("{\"message\":\"Category with name = Music already exists\"}");
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