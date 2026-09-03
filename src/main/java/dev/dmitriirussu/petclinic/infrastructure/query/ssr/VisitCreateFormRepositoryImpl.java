package dev.dmitriirussu.petclinic.infrastructure.query.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.visit.ssr.VisitCreateView;
import dev.dmitriirussu.petclinic.infrastructure.SqlLoader;
import dev.dmitriirussu.petclinic.infrastructure.query.support.ViewExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;


@RequiredArgsConstructor
class VisitCreateFormRepositoryImpl implements VisitCreateFormRepository {

    private final JdbcClient jdbc;
    private static final String VISIT_CREATE_FORM_SQL =
            SqlLoader.load("sql/query/ssr-visit-create-form.sql");

    @Override
    @Cacheable(cacheNames = "visitCreateForm", key = "#petId")
    public VisitCreateView findByPetId(String petId) {
        return jdbc.sql(VISIT_CREATE_FORM_SQL)
                .param("petId", petId)
                .query(ViewExtractor::getVisitCreateForm)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Pet not found: " + petId));
    }
}