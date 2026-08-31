package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.CreatePetCommand;

public interface PetCreateUseCase {
    String createPet(CreatePetCommand command);
}
