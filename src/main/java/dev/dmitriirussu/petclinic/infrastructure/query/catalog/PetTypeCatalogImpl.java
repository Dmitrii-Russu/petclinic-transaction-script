package dev.dmitriirussu.petclinic.infrastructure.query.catalog;

import dev.dmitriirussu.petclinic.application.query.catalog.PetTypeCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@RequiredArgsConstructor
final class PetTypeCatalogImpl implements PetTypeCatalog {
    private final JdbcClient jdbc;

    @Override
    public List<String> getAllTypes() {
        String sql = "SELECT name FROM pet_types ORDER BY name";
        return jdbc.sql(sql).query(String.class).list();
    }
}