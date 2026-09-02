package dev.dmitriirussu.petclinic.application.query.repository;

import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerNameView;

public interface SsrOwnerNameRepository {
    SsrOwnerNameView findByOwnerId(String ownerId);
}