package dev.dmitriirussu.petclinic.application.query.service.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerNameUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.PetEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.VisitCreateFormUseCase;

public final class SsrQueryServiceFactory {

    private SsrQueryServiceFactory() {}

    public static OwnerEditFormUseCase ownerEditFormUseCase(OwnerEditFormRepository repository) {
        return new OwnerEditFormService(repository);
    }

    public static OwnerNameUseCase ownerNameUseCase(OwnerNameRepository repository) {
        return new OwnerNameService(repository);
    }

    public static PetEditFormUseCase petEditFormUseCase(PetEditFormRepository repository) {
        return new PetEditFormService(repository);
    }

    public static VisitCreateFormUseCase visitCreateFormUseCase(VisitCreateFormRepository repository) {
        return new VisitCreateFormService(repository);
    }
}
