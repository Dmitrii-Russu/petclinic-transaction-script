package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.SsrOwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class SsrOwnerEditFormRepositoryImpl implements SsrOwnerEditFormRepository {
    private final JdbcClient jdbc;
    private static final String OWNER_EDIT_FORM_SQL =
            SqlLoader.load("sql/query/ssr-owner-edit-form.sql");

    @Override
    @Cacheable(cacheNames = "ownerEditForm", key = "#ownerId")
    public SsrOwnerEditView getOwnerEditFormByOwnerId(String ownerId) {
        return jdbc.sql(OWNER_EDIT_FORM_SQL)
                .param("ownerId", ownerId)
                .query(SsrOwnerEditView.class)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Owner not found: " + ownerId));
    }
}
