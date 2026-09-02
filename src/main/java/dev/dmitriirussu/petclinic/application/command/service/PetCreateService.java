package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.model.PetCreateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.PetCreateUseCase;
import dev.dmitriirussu.petclinic.model.Pet;
import dev.dmitriirussu.petclinic.application.command.repository.PetCreateRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
final class PetCreateService implements PetCreateUseCase {

    private final PetCreateRepository repository;

    public void createPet(PetCreateCommand command) {

        repository.create(
                new Pet(
                    UUID.randomUUID().toString(),
                    command.name(),
                    command.birthDate(),
                    command.type(),
                    command.ownerId()));
    }
}
