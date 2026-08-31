package dev.dmitriirussu.petclinic.application.query.view.visit;

import java.time.LocalDate;
import java.util.List;

public record SsrVisitCreateView(
        String petId,
        String petName,
        LocalDate birthDate,
        String type,
        String ownerId,
        String ownerFirstName,
        String ownerLastName,
        List<VisitView> visits
) {
    public SsrVisitCreateView {
        visits = List.copyOf(visits);
    }
}
