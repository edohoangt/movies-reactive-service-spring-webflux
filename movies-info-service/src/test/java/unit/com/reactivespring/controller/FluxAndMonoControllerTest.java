package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void should_ReturnAllItemsInFlux_When_GivenProperFlux() {
        webTestClient
                .get().uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class).hasSize(3);
    }

    @Test
    void should_ReturnCorrectItems_When_GivenProperFlux() {
        var flux = webTestClient
                .get().uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier
                .create(flux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    void should_ReturnAllItemsInFlux2_When_GivenProperFlux() {
        webTestClient
                .get().uri("/flux")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var responseBody = listEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(responseBody).size() == 3;
                });
    }

    @Test
    void should_Return1Item_When_GivenProperMono() {
        webTestClient
                .get().uri("/mono")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("Hello-world", responseBody);
                });
    }

    @Test
    void should_ReturnCorrectItems_When_GivenProperStreamingEndpoint() {
        var flux = webTestClient
                .get().uri("/stream")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier
                .create(flux)
                .expectNext(0L, 1L, 2L)
                .thenCancel()
                .verify();
    }
}