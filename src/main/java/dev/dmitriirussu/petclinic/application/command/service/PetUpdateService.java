package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.model.PetUpdateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.PetUpdateUseCase;
import dev.dmitriirussu.petclinic.model.Pet;
import dev.dmitriirussu.petclinic.application.command.repository.PetUpdateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class PetUpdateService implements PetUpdateUseCase {

    private final PetUpdateRepository repository;

    public void updatePet(PetUpdateCommand command) {

        repository.update(
                new Pet(
                        command.id(),
                        command.name(),
                        command.birthDate(),
                        command.type(),
                        command.ownerId()
                )
        );
    }
}