package dev.dmitriirussu.petclinic.infrastructure.query.catalog.ssr;

import dev.dmitriirussu.petclinic.application.query.catalog.ssr.PetTypeCatalog;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class PetTypeCatalogFactory {

    private PetTypeCatalogFactory() {}

    public static PetTypeCatalog petTypeCatalog(JdbcClient jdbc) {
        return new PetTypeCatalogImpl(jdbc);
    }
}
