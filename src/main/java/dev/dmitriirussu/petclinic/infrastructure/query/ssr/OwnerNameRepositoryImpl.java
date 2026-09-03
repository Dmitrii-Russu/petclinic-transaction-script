package dev.dmitriirussu.petclinic.infrastructure.query.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerNameView;
import dev.dmitriirussu.petclinic.infrastructure.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class OwnerNameRepositoryImpl implements OwnerNameRepository {

    private final JdbcClient jdbc;
    private static final String OWNER_NAME_SQL =
            SqlLoader.load("sql/query/ssr-owner-name.sql");

    @Override
    @Cacheable(cacheNames = "ownerName", key = "#ownerId")
    public OwnerNameView findByOwnerId(String ownerId) {
        return jdbc.sql(OWNER_NAME_SQL)
                .param("ownerId", ownerId)
                .query(OwnerNameView.class)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Owner not found: " + ownerId));
    }
}