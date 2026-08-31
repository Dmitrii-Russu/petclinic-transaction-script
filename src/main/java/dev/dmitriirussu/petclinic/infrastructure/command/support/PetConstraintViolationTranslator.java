package dev.dmitriirussu.petclinic.infrastructure.command.support;

import dev.dmitriirussu.petclinic.model.Pet;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

public final class PetConstraintViolationTranslator {

    private static final ConstraintViolationTranslator<Pet> TRANSLATOR =
            new ConstraintViolationTranslator<>(Map.of(
                    "pk_pet", (error, pet, e) ->
                            new IllegalStateException("Pet with id - " + pet.id() + " already exists", e),
                    "uq_pet_owner", (error, pet, e) ->
                            new IllegalStateException("This pet is already registered for this owner", e),
                    "chk_pet_name_length", (error, pet, e) ->
                            new IllegalArgumentException("Pet name must be between 1 and 30 characters", e),
                    "chk_pet_birth_date_not_future", (error, pet, e) ->
                            new IllegalArgumentException("Birth date cannot be in the future", e),
                    "fk_pet_owner", (error, pet, e) ->
                            new IllegalArgumentException("Owner does not exist: " + pet.ownerId(), e),
                    "fk_pet_type", (error, pet, e) ->
                            new IllegalArgumentException("Unknown pet type: " + pet.type(), e)
            ));

    public static RuntimeException translate(DataIntegrityViolationException e, Pet pet) {
        return TRANSLATOR.translate(e, pet);
    }
}