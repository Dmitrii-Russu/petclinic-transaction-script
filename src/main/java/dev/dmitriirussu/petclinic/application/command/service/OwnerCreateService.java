package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.usecase.OwnerCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.model.CreateOwnerCommand;
import dev.dmitriirussu.petclinic.model.Owner;
import dev.dmitriirussu.petclinic.application.command.repository.OwnerCreateRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
final class OwnerCreateService implements OwnerCreateUseCase {
    private final OwnerCreateRepository repository;

    public String createOwner(CreateOwnerCommand command) {
        Owner owner = new Owner(
                UUID.randomUUID().toString(),
                command.firstName(),
                command.lastName(),
                command.street(),
                command.city(),
                command.telephone()
        );
        repository.insert(owner);
        return owner.id();
    }
}