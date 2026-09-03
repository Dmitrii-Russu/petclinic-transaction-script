package dev.dmitriirussu.petclinic.infrastructure.query.catalog.ssr;

import dev.dmitriirussu.petclinic.application.query.catalog.ssr.PetTypeCatalog;
import dev.dmitriirussu.petclinic.infrastructure.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@RequiredArgsConstructor
final class PetTypeCatalogImpl implements PetTypeCatalog {
    private final JdbcClient jdbc;
    private static final String FIND_PET_TYPES_SQL =
            SqlLoader.load("sql/query/ssr-find-pet-types.sql");

    @Override
    public List<String> findAllTypes() {
        return jdbc.sql(FIND_PET_TYPES_SQL).query(String.class).list();
    }
}