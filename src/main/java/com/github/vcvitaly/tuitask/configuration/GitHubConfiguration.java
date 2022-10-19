package com.github.vcvitaly.tuitask.configuration;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

/**
 * GitHubConfiguration.
 *
 * @author Vitalii Chura
 */
@Configuration
public class GitHubConfiguration {

    @Value("${vcs.github.token}")
    private String token;

    @Profile("local")
    @Bean
    public GitHub gitHub() {
        try {
            return new GitHubBuilder()
                    .withOAuthToken(token)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
