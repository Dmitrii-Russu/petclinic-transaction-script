package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.FindOwnerRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.infrastructure.query.support.ViewExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class FindOwnerRepositoryImpl implements FindOwnerRepository {
    private final JdbcClient jdbc;
    private static final String OWNER_DETAILS_SQL =
            SqlLoader.load("sql/query/find-owner.sql");

    @Override
    @Cacheable(cacheNames = "ownerDetails", key = "#id")
    public OwnerDetailsView findById(String id) {
        return jdbc.sql(OWNER_DETAILS_SQL)
                .param("id", id)
                .query(ViewExtractor::getOwnerDetails)
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Owner not found"));
    }
}