package dev.dmitriirussu.petclinic.application.query.view.pet;

import java.time.LocalDate;

public record SsrPetEditView(
        String id,
        String name,
        LocalDate birthDate,
        String type,
        String ownerId,
        String firstName,
        String lastName
) {}