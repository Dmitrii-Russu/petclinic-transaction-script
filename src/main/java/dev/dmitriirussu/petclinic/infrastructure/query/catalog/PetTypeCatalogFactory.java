package dev.dmitriirussu.petclinic.infrastructure.query.catalog;

import dev.dmitriirussu.petclinic.application.query.catalog.SsrPetTypeCatalog;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class PetTypeCatalogFactory {

    private PetTypeCatalogFactory() {}

    public static SsrPetTypeCatalog petTypeCatalog(JdbcClient jdbc) {
        return new SsrPetTypeCatalogImpl(jdbc);
    }
}
