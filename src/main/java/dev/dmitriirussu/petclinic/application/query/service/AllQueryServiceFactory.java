package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.application.query.usecase.*;

public final class AllQueryServiceFactory {

    private AllQueryServiceFactory() {}

    public static FindOwnerUseCase findOwnerUseCase(FindOwnerRepository repository) {
        return new FindOwnerService(repository);
    }

    public static FindOwnerListUseCase findOwnerListUseCase(FindOwnerListRepository repository) {
        return new FindOwnerListService(repository);
    }

    public static SsrOwnerEditFormUseCase ssrOwnerEditFormUseCase(SsrOwnerEditFormRepository repository) {
        return new SsrOwnerEditFormService(repository);
    }

    public static SsrOwnerNameUseCase ssrOwnerNameUseCase(SsrOwnerNameRepository repository) {
        return new SsrOwnerNameService(repository);
    }

    public static SsrPetEditFormUseCase ssrPetEditFormUseCase(SsrPetEditFormRepository repository) {
        return new SsrPetEditFormService(repository);
    }

    public static SsrVisitCreateFormUseCase ssrVisitCreateFormUseCase(SsrVisitCreateFormRepository repository) {
        return new SsrVisitCreateFormService(repository);
    }
}