package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.SsrOwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerNameView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class SsrOwnerNameRepositoryImpl implements SsrOwnerNameRepository {

    private final JdbcClient jdbc;
    private static final String OWNER_NAME_SQL =
            SqlLoader.load("sql/query/ssr-owner-name.sql");

    @Override
    @Cacheable(cacheNames = "ownerName", key = "#ownerId")
    public SsrOwnerNameView findByOwnerId(String ownerId) {
        return jdbc.sql(OWNER_NAME_SQL)
                .param("ownerId", ownerId)
                .query(SsrOwnerNameView.class)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Owner not found: " + ownerId));
    }
}