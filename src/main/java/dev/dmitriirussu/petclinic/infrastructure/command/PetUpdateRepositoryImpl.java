package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.infrastructure.command.support.PetConstraintViolationTranslator;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.model.Pet;
import dev.dmitriirussu.petclinic.application.command.repository.PetUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

import org.springframework.cache.Cache;

@RequiredArgsConstructor
class PetUpdateRepositoryImpl implements PetUpdateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String PET_UPDATE_SQL =
            SqlLoader.load("sql/command/pet-update.sql");

    @Override
    public void update(Pet pet) {
        /*
         * Optimistic pre-filter, not source of truth — see README
         * "Optimistic duplicate pre-check via cache".
         *
         * Symmetric with OwnerUpdateRepositoryImpl: the "pet" cache is keyed
         * both by id and by the uq_pet_owner business key, so the previous
         * business key is available from the cache without an extra DB read,
         * and the stale entry under the old key gets evicted after a
         * successful write. DB UNIQUE(uq_pet_owner) remains authoritative;
         * a cache hit here only avoids an avoidable round-trip, see:
         * https://dmitrii-russu.github.io/posts/cache-pre-filter/
         */
        Cache cache = cacheManager.getCache("pet");
        Pet previous = cache.get(pet.id(), Pet.class);
        boolean keyChanged = previous == null || keyFieldsChanged(previous, pet);

        String newKey = PetCreateRepositoryImpl.petCacheKey(
                pet.ownerId(), pet.name(), pet.birthDate(), pet.type());

        if (keyChanged) {
            Pet cached = cache.get(newKey, Pet.class);
            if (cached != null && !cached.id().equals(pet.id())) {
                throw new IllegalStateException(
                        "This owner already has such pet: " + pet.name()
                                + " (" + pet.birthDate() + ", " + pet.type() + ")"
                );
            }
        }

        try {
            int rows = jdbc.sql(PET_UPDATE_SQL).paramSource(pet).update();
            if (rows != 1) {
                throw new NoSuchElementException("Pet not found - id: " + pet.id());
            }
        } catch (DataIntegrityViolationException e) {
            throw PetConstraintViolationTranslator.translate(e, pet);
        }

        if (previous != null && keyChanged) {
            String oldKey = PetCreateRepositoryImpl.petCacheKey(
                    previous.ownerId(), previous.name(), previous.birthDate(), previous.type());
            cache.evict(oldKey);
        }
        cache.put(newKey, pet);
        cache.put(pet.id(), pet);

        cacheManager.getCache("ownerDetails").evict(pet.ownerId());
        cacheManager.getCache("petEditForm").evict(pet.id());
    }

    private static boolean keyFieldsChanged(Pet previous, Pet pet) {
        return !previous.ownerId().equals(pet.ownerId())
                || !previous.name().equals(pet.name())
                || !previous.birthDate().equals(pet.birthDate())
                || !previous.type().equals(pet.type());
    }
}