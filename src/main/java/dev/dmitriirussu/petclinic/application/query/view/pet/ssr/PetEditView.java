package dev.dmitriirussu.petclinic.application.query.view.pet.ssr;

import java.time.LocalDate;

public record PetEditView(
        String id,
        String name,
        LocalDate birthDate,
        String type,
        String ownerId,
        String firstName,
        String lastName
) {}