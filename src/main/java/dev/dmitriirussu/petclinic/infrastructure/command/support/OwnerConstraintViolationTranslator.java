package dev.dmitriirussu.petclinic.infrastructure.command.support;

import dev.dmitriirussu.petclinic.model.Owner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

public final class OwnerConstraintViolationTranslator {

    private static final ConstraintViolationTranslator<Owner> TRANSLATOR =
            new ConstraintViolationTranslator<>(Map.of(
                    "pk_owner", (error, owner, e) ->
                            new IllegalStateException("Owner with id - " + owner.id() + " already exists", e),
                    "uq_owner_telephone", (error, owner, e) ->
                            new IllegalStateException("Owner with this telephone already exists: " + owner.telephone(), e),
                    "chk_owner_first_name_length", (error, owner, e) ->
                            new IllegalArgumentException("First name must be between 2 and 50 characters", e),
                    "chk_owner_last_name_length", (error, owner, e) ->
                            new IllegalArgumentException("Last name must be between 2 and 50 characters", e),
                    "chk_owner_street_length", (error, owner, e) ->
                            new IllegalArgumentException("Street must be between 1 and 100 characters", e),
                    "chk_owner_city_length", (error, owner, e) ->
                            new IllegalArgumentException("City must be between 1 and 50 characters", e)
            ));

    public static RuntimeException translate(DataIntegrityViolationException e, Owner owner) {
        return TRANSLATOR.translate(e, owner);
    }
}