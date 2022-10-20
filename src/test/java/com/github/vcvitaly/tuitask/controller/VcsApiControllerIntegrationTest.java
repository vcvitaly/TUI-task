package com.github.vcvitaly.tuitask.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.vcvitaly.tuitask.util.ResourceUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * VcsApiControllerIntegrationTest.
 *
 * @author Vitalii Chura
 */
@ContextConfiguration(classes = VcsApiControllerIntegrationTest.TestGitHubConfiguration.class)
class VcsApiControllerIntegrationTest extends ControllerIntegrationTestTemplate {

    private static final String USERNAME = "vcvitaly";

    @Autowired
    private WebTestClient webTestClient;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("vcs.github.endpoint", wireMockServer::baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
    }

    @Test
    void aListOfVcsInfoDetailsIsReturned() {
        wireMockServer.stubFor(
                get(String.format("/users/%s", USERNAME))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("user.json")
                                        .withStatus(200)
                        )
        );
        wireMockServer.stubFor(
                get(String.format("/users/%s/repos?per_page=100", USERNAME))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("repos.json")
                                        .withStatus(200)
                        )
        );
        wireMockServer.stubFor(
                get(String.format("/repos/%s/Cards-shuffler/branches", USERNAME))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("card_shuffler_branches.json")
                                        .withStatus(200)
                        )
        );
        wireMockServer.stubFor(
                get(String.format("/repos/%s/docker-graphite-statsd/branches", USERNAME))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile("docker-graphite-statsd-branches.json")
                                        .withStatus(200)
                        )
        );

        webTestClient
                .get()
                .uri(String.format("/api/v1/vcs-api/%s/repos/github", USERNAME))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtil.readResourceAsString("test_data/github_vcs_details.json"));
    }


    @TestConfiguration
    static class TestGitHubConfiguration {

        @Value("${vcs.github.endpoint}")
        private String endpoint;

        @Value("${vcs.github.token}")
        private String token;

        @Profile("test")
        @Bean
        public GitHub gitHubTestInstance() {
            try {
                return new GitHubBuilder()
                        .withEndpoint(endpoint)
                        .withOAuthToken(token)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}