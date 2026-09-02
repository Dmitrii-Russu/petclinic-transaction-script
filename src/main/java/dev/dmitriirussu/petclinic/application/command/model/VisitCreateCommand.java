package dev.dmitriirussu.petclinic.application.command.model;

import java.time.LocalDate;

public record VisitCreateCommand(
        LocalDate visitDate,
        String description,
        String petId,
        String ownerId
) {}
