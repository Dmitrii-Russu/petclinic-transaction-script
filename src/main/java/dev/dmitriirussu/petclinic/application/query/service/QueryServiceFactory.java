package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.application.query.usecase.*;

public final class QueryServiceFactory {

    private QueryServiceFactory() {}

    public static OwnerFindUseCase ownerFindUseCase(OwnerFindRepository repository) {
        return new OwnerFindService(repository);
    }

    public static OwnerSearchUseCase ownerSearchUseCase(OwnerSearchRepository repository) {
        return new OwnerSearchService(repository);
    }
}