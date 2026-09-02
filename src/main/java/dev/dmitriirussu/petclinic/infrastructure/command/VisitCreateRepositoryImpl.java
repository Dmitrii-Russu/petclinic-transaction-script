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
    private static final String VISIT_CREATE_SQL =
            SqlLoader.load("sql/command/visit-create.sql");

    @Override
    public void create(Visit visit, String ownerId) {
        /*
         * Optimistic pre-filter, not source of truth — see README
         * "Optimistic duplicate pre-check via cache".
         *
         * DB UNIQUE(pet_id, visit_date) remains authoritative; a cache hit
         * here only avoids an avoidable round-trip, see:
         * https://dmitrii-russu.github.io/posts/cache-pre-filter/
         */
        Cache cache = cacheManager.getCache("visit");
        String cacheKey = visitCacheKey(visit.petId(), visit.visitDate());

        Visit cached = cache.get(cacheKey, Visit.class);
        if (cached != null) {
            throw new IllegalStateException(
                    "This pet already has a visit on this date: " + visit.visitDate()
            );
        }

        try {
            jdbc.sql(VISIT_CREATE_SQL).paramSource(visit).update();
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