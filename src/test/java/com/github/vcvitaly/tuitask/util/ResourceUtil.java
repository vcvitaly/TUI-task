package com.github.vcvitaly.tuitask.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

/**
 * ResourceUtil.
 *
 * @author Vitalii Chura
 */
public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static String readResourceAsString(String fileName) {
        try {
            var file = getFileFromResource(fileName);
            return Files.readString(file.toPath());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
