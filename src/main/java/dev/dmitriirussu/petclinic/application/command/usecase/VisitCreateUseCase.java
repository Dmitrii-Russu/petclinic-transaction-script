package dev.dmitriirussu.petclinic.application.command.usecase;

import dev.dmitriirussu.petclinic.application.command.model.CreateVisitCommand;

public interface VisitCreateUseCase {
    String createVisit(CreateVisitCommand command);
}
