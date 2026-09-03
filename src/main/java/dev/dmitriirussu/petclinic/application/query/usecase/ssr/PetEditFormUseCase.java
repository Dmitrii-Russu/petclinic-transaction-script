package dev.dmitriirussu.petclinic.application.query.usecase.ssr;

import dev.dmitriirussu.petclinic.application.query.view.pet.ssr.PetEditView;

public interface PetEditFormUseCase {
    PetEditView findPetEditForm(String petId);
}
