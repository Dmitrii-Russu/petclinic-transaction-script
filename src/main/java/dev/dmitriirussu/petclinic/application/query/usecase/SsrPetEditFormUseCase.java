package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.pet.SsrPetEditView;

public interface SsrPetEditFormUseCase {
    SsrPetEditView getPetEditForm(String petId);
}
