package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.SsrPetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrPetEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.pet.SsrPetEditView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SsrPetEditFormService implements SsrPetEditFormUseCase {
    private final SsrPetEditFormRepository repository;

    public SsrPetEditView getPetEditFormById(String petId) {
        return repository.getPetEditFormById(petId);
    }
}