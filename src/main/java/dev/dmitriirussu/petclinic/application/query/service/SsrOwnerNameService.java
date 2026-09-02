package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.SsrOwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrOwnerNameUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerNameView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SsrOwnerNameService implements SsrOwnerNameUseCase {
    private final SsrOwnerNameRepository repository;

    public SsrOwnerNameView getOwnerNameByOwnerId(String ownerId) {
        return repository.getOwnerNameByOwnerId(ownerId);
    }
}