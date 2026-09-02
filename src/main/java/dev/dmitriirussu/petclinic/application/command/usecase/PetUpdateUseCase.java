package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.PetUpdateCommand;

public interface PetUpdateUseCase {
    void updatePet(PetUpdateCommand command);
}
