package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.FindOwnerRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import lombok.RequiredArgsConstructor;

/** Shared across SSR and REST — see {@link dev.dmitriirussu.petclinic.application.query} for the find/get convention. */
@RequiredArgsConstructor
final class FindOwnerService implements FindOwnerUseCase {
    private final FindOwnerRepository repository;

    public OwnerDetailsView findByOwnerId(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}