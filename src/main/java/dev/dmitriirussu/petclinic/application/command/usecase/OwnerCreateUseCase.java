package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.OwnerCreateCommand;

public interface OwnerCreateUseCase {
    String createOwner(OwnerCreateCommand command);
}
