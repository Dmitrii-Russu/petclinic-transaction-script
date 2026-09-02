package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;

public interface SsrOwnerEditFormUseCase {
    SsrOwnerEditView getOwnerEditForm(String ownerId);
}
