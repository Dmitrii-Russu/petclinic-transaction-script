package dev.dmitriirussu.petclinic.application.query.service.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.VisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.visit.ssr.VisitCreateView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class VisitCreateFormService implements VisitCreateFormUseCase {

    private final VisitCreateFormRepository repository;

    public VisitCreateView findVisitCreateForm(String petId) {
        return repository.findByPetId(petId);
    }
}