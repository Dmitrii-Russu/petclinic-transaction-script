package dev.dmitriirussu.petclinic.model;

import java.time.LocalDate;

public record Visit(
        String id,
        LocalDate visitDate,
        String description,
        String petId
) {}