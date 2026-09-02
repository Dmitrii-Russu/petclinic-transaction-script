package dev.dmitriirussu.petclinic.application.query.repository;

import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;

public interface SsrOwnerEditFormRepository {
    SsrOwnerEditView getOwnerEditFormByOwnerId(String ownerId);
}