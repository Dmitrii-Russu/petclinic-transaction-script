package dev.dmitriirussu.petclinic.application.query.view.visit;

import java.time.LocalDate;

public record VisitView(
        String id,
        LocalDate date,
        String description
) {}
