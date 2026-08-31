package dev.dmitriirussu.petclinic.application.command.model;

import java.time.LocalDate;

public record UpdatePetCommand(
        String id,
        String name,
        LocalDate birthDate,
        String type,
        String ownerId
) {}

