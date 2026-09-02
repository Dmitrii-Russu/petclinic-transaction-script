package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.VisitCreateCommand;

public interface VisitCreateUseCase {
    void createVisit(VisitCreateCommand command);
}
