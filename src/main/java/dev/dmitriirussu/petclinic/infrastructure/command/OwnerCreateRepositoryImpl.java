package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.application.command.repository.OwnerCreateRepository;
import dev.dmitriirussu.petclinic.infrastructure.command.support.OwnerConstraintViolationTranslator;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.model.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
class OwnerCreateRepositoryImpl implements OwnerCreateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String OWNER_CREATE_SQL =
                SqlLoader.load("sql/command/owner-create.sql");

    @Override
    public void create(Owner owner) {
        /*
         * Optimistic pre-filter, not source of truth — see README
         * "Optimistic duplicate pre-check via cache".
         *
         * DB UNIQUE(telephone) remains authoritative; a cache hit
         * here only avoids an avoidable round-trip, see:
         * https://dmitrii-russu.github.io/posts/cache-pre-filter/
         */
        Cache cache = cacheManager.getCache("owner");
        Owner cached = cache.get(owner.telephone(), Owner.class);

        if (cached != null) {
            throw new IllegalStateException(
                    "Owner with this telephone already exists: " + cached.telephone()
            );
        }

        try {
            jdbc.sql(OWNER_CREATE_SQL).paramSource(owner).update();
        } catch (DataIntegrityViolationException e) {
            throw OwnerConstraintViolationTranslator.translate(e, owner);
        }

        cache.put(owner.id(), owner);
        cache.put(owner.telephone(), owner);
    }
}
