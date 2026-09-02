package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.PetCreateCommand;

public interface PetCreateUseCase {
    void createPet(PetCreateCommand command);
}
