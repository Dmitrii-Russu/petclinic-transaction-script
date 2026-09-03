package dev.dmitriirussu.petclinic.application.query.repository.ssr;

import dev.dmitriirussu.petclinic.application.query.view.pet.ssr.PetEditView;

public interface PetEditFormRepository {
    PetEditView findByPetId(String petId);
}
