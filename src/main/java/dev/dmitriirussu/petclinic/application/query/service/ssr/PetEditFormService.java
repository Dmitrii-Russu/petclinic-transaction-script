package dev.dmitriirussu.petclinic.application.query.service.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.PetEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.pet.ssr.PetEditView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class PetEditFormService implements PetEditFormUseCase {

    private final PetEditFormRepository repository;

    public PetEditView findPetEditForm(String petId) {
        return repository.findByPetId(petId);
    }
}