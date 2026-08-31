package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.SsrVisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrVisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SsrVisitCreateFormService implements SsrVisitCreateFormUseCase {
    private final SsrVisitCreateFormRepository repository;

    public SsrVisitCreateView getVisitCreateFormByPetId(String petId) {
        return repository.getVisitCreateFormByPetId(petId);
    }
}