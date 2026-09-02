package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.model.VisitCreateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.VisitCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.repository.VisitCreateRepository;
import dev.dmitriirussu.petclinic.model.Visit;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
final class VisitCreateService implements VisitCreateUseCase {
    private final VisitCreateRepository repository;

    public void createVisit(VisitCreateCommand command) {

        Visit visit = new Visit(
                UUID.randomUUID().toString(),
                command.visitDate(),
                command.description(),
                command.petId()
        );
        repository.create(visit, command.ownerId());
    }
}