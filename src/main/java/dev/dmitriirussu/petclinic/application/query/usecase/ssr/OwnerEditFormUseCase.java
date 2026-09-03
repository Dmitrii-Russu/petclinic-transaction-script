package dev.dmitriirussu.petclinic.application.query.usecase.ssr;

import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerEditView;

public interface OwnerEditFormUseCase {
    OwnerEditView findOwnerEditForm(String ownerId);
}
