package com.github.vcvitaly.tuitask.service.impl;

import com.github.vcvitaly.tuitask.exception.VcsApiCommunicationIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}