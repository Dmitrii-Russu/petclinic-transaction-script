package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;

public interface OwnerFindUseCase {
    OwnerDetailsView findById(String id);
}
