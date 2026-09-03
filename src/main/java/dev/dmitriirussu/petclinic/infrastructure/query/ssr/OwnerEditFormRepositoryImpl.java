package dev.dmitriirussu.petclinic.infrastructure.query.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerEditView;
import dev.dmitriirussu.petclinic.infrastructure.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class OwnerEditFormRepositoryImpl implements OwnerEditFormRepository {

    private final JdbcClient jdbc;
    private static final String OWNER_EDIT_FORM_SQL =
            SqlLoader.load("sql/query/ssr-owner-edit-form.sql");

    @Override
    @Cacheable(cacheNames = "ownerEditForm", key = "#ownerId")
    public OwnerEditView findByOwnerId(String ownerId) {
        return jdbc.sql(OWNER_EDIT_FORM_SQL)
                .param("ownerId", ownerId)
                .query(OwnerEditView.class)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Owner not found: " + ownerId));
    }
}
