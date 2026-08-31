package dev.dmitriirussu.petclinic.model;

import java.time.LocalDate;

public record Pet(
        String id,
        String name,
        LocalDate birthDate,
        String type,
        String ownerId
) {}
