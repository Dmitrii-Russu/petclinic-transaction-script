package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.CreateOwnerCommand;

public interface OwnerCreateUseCase {
    String createOwner(CreateOwnerCommand command);
}
