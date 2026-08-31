package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.UpdateOwnerCommand;

public interface OwnerUpdateUseCase {
    void updateOwner(UpdateOwnerCommand command);
}
