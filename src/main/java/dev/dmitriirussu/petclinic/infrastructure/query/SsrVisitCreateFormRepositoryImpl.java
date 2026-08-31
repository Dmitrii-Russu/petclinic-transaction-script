package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.SsrVisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.infrastructure.query.support.ViewExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class SsrVisitCreateFormRepositoryImpl implements SsrVisitCreateFormRepository {
    private final JdbcClient jdbc;
    private static final String FIND_CREATE_VIEW_SQL =
            SqlLoader.load("sql/query/ssr-visit-create-form.sql");

    @Override
    @Cacheable(cacheNames = "visitCreateForm", key = "#petId")
    public SsrVisitCreateView getVisitCreateFormByPetId(String petId) {
        return jdbc.sql(FIND_CREATE_VIEW_SQL)
                .param("petId", petId)
                .query(ViewExtractor::getCreateVisitForm)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Pet not found: " + petId));
    }
}