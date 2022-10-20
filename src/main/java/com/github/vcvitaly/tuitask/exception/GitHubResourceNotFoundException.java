package com.github.vcvitaly.tuitask.exception;

/**
 * GitHubUserNotFoundException.
 *
 * @author Vitalii Chura
 */
public class GitHubResourceNotFoundException extends RuntimeException {

    public GitHubResourceNotFoundException(String resourceName, Exception cause) {
        super(String.format("GitHub resource %s not found", resourceName), cause);
    }
}
