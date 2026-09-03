package dev.dmitriirussu.petclinic.application.query.usecase.ssr;

import dev.dmitriirussu.petclinic.application.query.view.visit.ssr.VisitCreateView;

public interface VisitCreateFormUseCase {
    VisitCreateView findVisitCreateForm(String petId);
}
