package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.OwnerFindRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.OwnerFindUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class OwnerFindService implements OwnerFindUseCase {
    private final OwnerFindRepository repository;

    public OwnerDetailsView findById(String id) {
        return repository.findById(id);
    }
}