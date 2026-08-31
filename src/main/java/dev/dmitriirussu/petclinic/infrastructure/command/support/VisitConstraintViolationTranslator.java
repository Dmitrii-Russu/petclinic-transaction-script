package dev.dmitriirussu.petclinic.infrastructure.command.support;

import dev.dmitriirussu.petclinic.model.Visit;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

public final class VisitConstraintViolationTranslator {

    private static final ConstraintViolationTranslator<Visit> TRANSLATOR =
            new ConstraintViolationTranslator<>(Map.of(
                    "pk_visit", (error, visit, e) ->
                            new IllegalStateException("Visit with id - " + visit.id() + " already exists", e),
                    "uq_visit_pet_date", (error, visit, e) ->
                            new IllegalStateException("This pet already has a visit on this date", e),
                    "chk_visit_description_length", (error, visit, e) ->
                            new IllegalArgumentException("Description must be between 1 and 500 characters", e),
                    "fk_visit_pet", (error, visit, e) ->
                            new IllegalArgumentException("Pet does not exist: " + visit.petId(), e)
            ));

    public static RuntimeException translate(DataIntegrityViolationException e, Visit visit) {
        return TRANSLATOR.translate(e, visit);
    }
}