package com.aram.flashcards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.TEXT_HTML;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    void swaggerUserInterfaceIsLoadedCorrectly() {
        client.get().uri("/swagger-ui/index.html")
                .accept(TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(containsString("<title>Swagger UI</title>"));
    }

}
