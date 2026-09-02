package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;

public interface SsrVisitCreateFormUseCase {
    SsrVisitCreateView getVisitCreateForm(String petId);
}
