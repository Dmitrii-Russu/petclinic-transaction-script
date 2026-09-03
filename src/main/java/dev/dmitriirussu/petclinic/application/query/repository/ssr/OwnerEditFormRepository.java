package dev.dmitriirussu.petclinic.application.query.repository.ssr;

import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerEditView;

public interface OwnerEditFormRepository {
    OwnerEditView findByOwnerId(String ownerId);
}