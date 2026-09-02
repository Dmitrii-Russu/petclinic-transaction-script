package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerNameView;

public interface SsrOwnerNameUseCase {
    SsrOwnerNameView getOwnerNameByOwnerId(String ownerId);
}