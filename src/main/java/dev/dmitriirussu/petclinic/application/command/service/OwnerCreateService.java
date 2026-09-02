package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.model.OwnerCreateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.OwnerCreateUseCase;
import dev.dmitriirussu.petclinic.model.Owner;
import dev.dmitriirussu.petclinic.application.command.repository.OwnerCreateRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
final class OwnerCreateService implements OwnerCreateUseCase {
    private final OwnerCreateRepository repository;

    public String createOwner(OwnerCreateCommand command) {
        Owner owner = new Owner(
                UUID.randomUUID().toString(),
                command.firstName(),
                command.lastName(),
                command.street(),
                command.city(),
                command.telephone()
        );
        repository.create(owner);
        return owner.id();
    }
}