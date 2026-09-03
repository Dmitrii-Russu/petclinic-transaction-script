package dev.dmitriirussu.petclinic.application.query.view.visit.ssr;

import dev.dmitriirussu.petclinic.application.query.view.visit.VisitView;

import java.time.LocalDate;
import java.util.List;

public record VisitCreateView(
        String petId,
        String petName,
        LocalDate birthDate,
        String type,
        String ownerId,
        String ownerFirstName,
        String ownerLastName,
        List<VisitView> visits
) {
    public VisitCreateView {
        visits = List.copyOf(visits);
    }
}
