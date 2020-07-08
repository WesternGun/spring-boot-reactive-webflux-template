package com.example.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class ITestClient {
    private final WebTestClient client;

    public WebTestClient.ResponseSpec create() {
        return client.mutate()
                .build()
                .get().uri("/order/create")
                .accept(APPLICATION_JSON)
                .exchange();
    }
}
