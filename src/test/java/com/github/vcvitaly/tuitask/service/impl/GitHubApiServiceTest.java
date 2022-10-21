package com.github.vcvitaly.tuitask.service.impl;

import com.github.vcvitaly.tuitask.exception.GitHubResourceNotFoundException;
import com.github.vcvitaly.tuitask.exception.VcsApiCommunicationIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GitHub;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * GitHubApiServiceTest.
 *
 * @author Vitalii Chura
 */
class GitHubApiServiceTest {

    private static final String TEST_USER_NAME = "testUserName";
    private static final String INCORRECT_USER_NAME = "incorrectUserName";
    private GitHubApiService gitHubApiService;
    private GitHub gitHub;

    @BeforeEach
    void setUp() {
        gitHub = mock(GitHub.class);
        gitHubApiService = new GitHubApiService(gitHub);
    }

    @Test
    void anIOExceptionIsCaughtAndRethrown() throws IOException {
        when(gitHub.getUser(TEST_USER_NAME)).thenThrow(IOException.class);

        assertThatThrownBy(() -> gitHubApiService.getVcsDetails(TEST_USER_NAME))
                .isInstanceOf(VcsApiCommunicationIOException.class);
    }

    @Test
    void aFileNotFoundExceptionIsCaughtAndRethrown() throws IOException {
        when(gitHub.getUser(INCORRECT_USER_NAME))
                .thenThrow(new GHFileNotFoundException(
                        String.format("API_URL/users/%s {\"Not Found\"}", INCORRECT_USER_NAME)
                ));

        assertThatThrownBy(() -> gitHubApiService.getVcsDetails(INCORRECT_USER_NAME))
                .isInstanceOf(GitHubResourceNotFoundException.class)
                .hasMessage(String.format("GitHub user %s not found", INCORRECT_USER_NAME));
    }
}