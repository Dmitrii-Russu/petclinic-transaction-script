package dev.dmitriirussu.petclinic.application.query.usecase.ssr;

import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerNameView;

public interface OwnerNameUseCase {
    OwnerNameView findOwnerName(String ownerId);
}