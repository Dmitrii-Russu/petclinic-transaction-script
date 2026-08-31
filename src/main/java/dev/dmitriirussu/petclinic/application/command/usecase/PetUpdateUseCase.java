package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.UpdatePetCommand;

public interface PetUpdateUseCase {
    void updatePet(UpdatePetCommand command);
}
