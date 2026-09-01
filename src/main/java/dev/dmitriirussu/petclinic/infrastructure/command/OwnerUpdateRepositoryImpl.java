package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.infrastructure.command.support.OwnerConstraintViolationTranslator;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.model.Owner;
import dev.dmitriirussu.petclinic.application.command.repository.OwnerUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class OwnerUpdateRepositoryImpl implements OwnerUpdateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String OWNER_UPDATE_SQL =
            SqlLoader.load("sql/command/owner-update.sql");

    @Override
    public void update(Owner owner) {
        /*
         * Optimistic pre-filter, not source of truth — see README
         * "Optimistic duplicate pre-check via cache".
         *
         * Unlike the Create-path check, this only fires when the phone
         * number actually changes (comparison against the cached "previous"
         * value) — updating an owner without touching telephone must not
         * trip the uq_owner_telephone pre-check. DB UNIQUE(telephone) remains
         * authoritative; a cache hit here only avoids an avoidable round-trip,
         * see: https://dmitrii-russu.github.io/posts/cache-pre-filter/
         */
        Cache ownerCache = cacheManager.getCache("owner");

        Owner previous = ownerCache.get(owner.id(), Owner.class);
        boolean phoneChanged = previous == null || !previous.telephone().equals(owner.telephone());

        if (phoneChanged) {
            Owner ownerWithSamePhone = ownerCache.get(owner.telephone(), Owner.class);
            if (ownerWithSamePhone != null && !ownerWithSamePhone.id().equals(owner.id())) {
                throw new IllegalStateException(
                        "Owner with this telephone already exists: " + owner.telephone()
                );
            }
        }

        try {
            int rows = jdbc.sql(OWNER_UPDATE_SQL).paramSource(owner).update();
            if (rows != 1) {
                throw new NoSuchElementException("Owner not found - id: " + owner.id());
            }
        } catch (DataIntegrityViolationException e) {
            throw OwnerConstraintViolationTranslator.translate(e, owner);
        }

        if (previous != null && phoneChanged) {
            ownerCache.evict(previous.telephone());
        }
        ownerCache.put(owner.id(), owner);
        ownerCache.put(owner.telephone(), owner);

        cacheManager.getCache("ownerDetails").evict(owner.id());
        cacheManager.getCache("ownerEditForm").evict(owner.id());
        cacheManager.getCache("ownerName").evict(owner.id());
    }
}