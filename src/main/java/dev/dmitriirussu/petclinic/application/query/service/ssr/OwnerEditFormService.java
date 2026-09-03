package dev.dmitriirussu.petclinic.application.query.service.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerEditView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class OwnerEditFormService implements OwnerEditFormUseCase {

    private final OwnerEditFormRepository repository;

    public OwnerEditView findOwnerEditForm(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}