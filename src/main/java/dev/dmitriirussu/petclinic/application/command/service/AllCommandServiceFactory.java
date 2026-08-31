package dev.dmitriirussu.petclinic.application.command.service;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.application.command.usecase.*;

public final class AllCommandServiceFactory {

    private AllCommandServiceFactory() {}

    public static OwnerCreateUseCase createOwnerUseCase(OwnerCreateRepository repository) {
        return new OwnerCreateService(repository);
    }

    public static OwnerUpdateUseCase updateOwnerUseCase(OwnerUpdateRepository repository) {
        return new OwnerUpdateService(repository);
    }

    public static PetCreateUseCase createPetUseCase(PetCreateRepository repository) {
        return new PetCreateService(repository);
    }

    public static PetUpdateUseCase updatePetUseCase(PetUpdateRepository repository) {
        return new PetUpdateService(repository);
    }

    public static VisitCreateUseCase createVisitUseCase(VisitCreateRepository repository) {
        return new VisitCreateService(repository);
    }
}