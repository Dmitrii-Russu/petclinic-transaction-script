package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.application.command.repository.PetCreateRepository;
import dev.dmitriirussu.petclinic.infrastructure.command.support.PetConstraintViolationTranslator;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.model.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDate;

@RequiredArgsConstructor
class PetCreateRepositoryImpl implements PetCreateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String PET_INSERT_SQL =
            SqlLoader.load("sql/command/pet-insert.sql");

    @Override
    public void insert(Pet pet) {
        Cache cache = cacheManager.getCache("pet");
        String cacheKey = petCacheKey(pet.ownerId(), pet.name(), pet.birthDate(), pet.type());

        Pet cached = cache.get(cacheKey, Pet.class);

        if (cached != null) {
            throw new IllegalStateException(
                    "This owner already has such pet: " + pet.name() + " (" + pet.birthDate() + ", " + pet.type() + ")"
            );
        }

        try {
            jdbc.sql(PET_INSERT_SQL).paramSource(pet).update();
        } catch (DataIntegrityViolationException e) {
            throw PetConstraintViolationTranslator.translate(e, pet);
        }

        cache.put(cacheKey, pet);
        cacheManager.getCache("ownerDetails").evict(pet.ownerId());
    }

    private static String petCacheKey(String ownerId, String name, LocalDate birthDate, String type) {
        return ownerId + "::" + name + "::" + birthDate + "::" + type;
    }
}
