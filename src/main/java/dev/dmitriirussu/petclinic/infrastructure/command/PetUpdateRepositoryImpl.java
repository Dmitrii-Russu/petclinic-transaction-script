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

@RequiredArgsConstructor
class PetUpdateRepositoryImpl implements PetUpdateRepository {
    private final JdbcClient jdbc;
    private final CacheManager cacheManager;
    private static final String PET_UPDATE_SQL =
            SqlLoader.load("sql/command/pet-update.sql");

    @Override
    public void update(Pet pet) {
        try {
            int rows = jdbc.sql(PET_UPDATE_SQL).paramSource(pet).update();
            if (rows != 1) {
                throw new NoSuchElementException("Pet not found - id: " + pet.id());
            }
        } catch (DataIntegrityViolationException e) {
            throw PetConstraintViolationTranslator.translate(e, pet);
        }

        cacheManager.getCache("ownerDetails").evict(pet.ownerId());
        cacheManager.getCache("petEditForm").evict(pet.id());
    }
}
