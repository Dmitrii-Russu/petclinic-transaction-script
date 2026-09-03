package dev.dmitriirussu.petclinic.application.query.service.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerNameUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerNameView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class OwnerNameService implements OwnerNameUseCase {

    private final OwnerNameRepository repository;

    public OwnerNameView findOwnerName(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}