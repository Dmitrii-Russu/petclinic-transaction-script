package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;

public interface FindOwnerUseCase {
    OwnerDetailsView findOwnerById(String id);
}
