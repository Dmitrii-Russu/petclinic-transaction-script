package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.SsrOwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrOwnerEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class SsrOwnerEditFormService implements SsrOwnerEditFormUseCase {

    private final SsrOwnerEditFormRepository repository;

    public SsrOwnerEditView getOwnerEditForm(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}