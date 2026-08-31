package dev.dmitriirussu.petclinic.application.query.view.pet;

import dev.dmitriirussu.petclinic.application.query.view.visit.VisitView;

import java.time.LocalDate;
import java.util.List;

public record PetDetailsView(
        String id,
        String name,
        LocalDate birthDate,
        String type,
        List<VisitView> visits
) {
    public PetDetailsView {
        visits = List.copyOf(visits);
    }
}