package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.infrastructure.command.support.VisitConstraintViolationTranslator;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.model.Visit;
import dev.dmitriirussu.petclinic.application.command.repository.VisitCreateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDate;

@RequiredArgsConstructor
class VisitCreateRepositoryImpl implements VisitCreateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String VISIT_INSERT_SQL =
            SqlLoader.load("sql/command/visit-insert.sql");

    @Override
    public void insert(Visit visit, String ownerId) {
        Cache cache = cacheManager.getCache("visit");
        String cacheKey = visitCacheKey(visit.petId(), visit.visitDate());

        Visit cached = cache.get(cacheKey, Visit.class);
        if (cached != null) {
            throw new IllegalStateException(
                    "This pet already has a visit on this date: " + visit.visitDate()
            );
        }

        try {
            jdbc.sql(VISIT_INSERT_SQL).paramSource(visit).update();
        } catch (DataIntegrityViolationException e) {
            throw VisitConstraintViolationTranslator.translate(e, visit);
        }

        cache.put(cacheKey, visit);
        cacheManager.getCache("ownerDetails").evict(ownerId);
        cacheManager.getCache("visitCreateForm").evict(visit.petId());
    }

    private static String visitCacheKey(String petId, LocalDate visitDate) {
        return petId + "::" + visitDate;
    }
}