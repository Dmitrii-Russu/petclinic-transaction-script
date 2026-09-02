package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.OwnerUpdateCommand;

public interface OwnerUpdateUseCase {
    void updateOwner(OwnerUpdateCommand command);
}
