package dev.dmitriirussu.petclinic.infrastructure;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public final class SqlLoader {
    private SqlLoader() {}

    public static String load(String classpathLocation) {
        try {
            return new ClassPathResource(classpathLocation).getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load SQL resource: " + classpathLocation, e);
        }
    }
}
