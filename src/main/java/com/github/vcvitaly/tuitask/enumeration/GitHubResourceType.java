package com.github.vcvitaly.tuitask.enumeration;

/**
 * GItHubResourceType.
 *
 * @author Vitalii Chura
 */
public enum GitHubResourceType {

    USER("user"),
    GENERAL_RESOURCE("resource");

    private final String name;

    GitHubResourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
