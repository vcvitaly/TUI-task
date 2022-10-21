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

    private static final String USERNAME = "TestUser";
    private static final String INCORRECT_USERNAME = "TestUser1";

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
        setUpWiremockForGetRequest("/users/%s", "user.json");
        setUpWiremockForGetRequest("/users/%s/repos?per_page=100", "repos.json");
        setUpWiremockForGetRequest("/repos/%s/TestRepo1/branches", "TestRepo1_branches.json");
        setUpWiremockForGetRequest("/repos/%s/TestRepo2/branches", "TestRepo2_branches.json");

        webTestClient
                .get()
                .uri(String.format("/api/v1/vcs-api/%s/github/repos", USERNAME))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtil.readResourceAsString("test_data/github_vcs_details.json"));
    }

    @Test
    void anErrorResponseIsReturnedUponIncorrectMediaType() {
        webTestClient
                .get()
                .uri(String.format("/api/v1/vcs-api/%s/github/repos", INCORRECT_USERNAME))
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .jsonPath("status")
                .isEqualTo(406)
                .jsonPath("message")
                .isEqualTo("Could not find acceptable representation, the only supported representation is application/json");
    }

    private static void setUpWiremockForGetRequest(String format, String fileName) {
        wireMockServer.stubFor(
                get(String.format(format, USERNAME))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBodyFile(fileName)
                                        .withStatus(200)
                        )
        );
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