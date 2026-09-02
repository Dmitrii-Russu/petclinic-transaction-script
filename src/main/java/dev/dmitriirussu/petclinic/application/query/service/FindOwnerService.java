package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.FindOwnerRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class FindOwnerService implements FindOwnerUseCase {
    private final FindOwnerRepository repository;

    public OwnerDetailsView findById(String id) {
        return repository.findById(id);
    }
}