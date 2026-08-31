package dev.dmitriirussu.petclinic.infrastructure.query.catalog;

import dev.dmitriirussu.petclinic.application.query.catalog.PetTypeCatalog;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class PetTypeCatalogReadFactory {

    private PetTypeCatalogReadFactory() {}

    public static PetTypeCatalog petTypeCatalog(JdbcClient jdbc) {
        return new PetTypeCatalogImpl(jdbc);
    }
}
