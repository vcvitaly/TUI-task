package com.github.vcvitaly.tuitask.exception;

import com.github.vcvitaly.tuitask.enumeration.GitHubResourceType;

/**
 * GitHubUserNotFoundException.
 *
 * @author Vitalii Chura
 */
public class GitHubResourceNotFoundException extends RuntimeException {

    public GitHubResourceNotFoundException(GitHubResourceType resourceType, String resourceName, Exception cause) {
        super(String.format("GitHub %s %s not found", resourceType.getName(), resourceName), cause);
    }
}
