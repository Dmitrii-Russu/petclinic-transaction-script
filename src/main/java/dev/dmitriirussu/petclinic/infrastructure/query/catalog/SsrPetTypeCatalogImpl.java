package dev.dmitriirussu.petclinic.infrastructure.query.catalog;

import dev.dmitriirussu.petclinic.application.query.catalog.SsrPetTypeCatalog;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@RequiredArgsConstructor
final class SsrPetTypeCatalogImpl implements SsrPetTypeCatalog {
    private final JdbcClient jdbc;
    private static final String FIND_PET_TYPES_SQL =
            SqlLoader.load("sql/query/ssr-find-pet-types.sql");

    @Override
    public List<String> getAllTypes() {
        return jdbc.sql(FIND_PET_TYPES_SQL).query(String.class).list();
    }
}