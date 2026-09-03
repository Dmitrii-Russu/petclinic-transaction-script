package dev.dmitriirussu.petclinic.application.query.repository.ssr;

import dev.dmitriirussu.petclinic.application.query.view.owner.ssr.OwnerNameView;

public interface OwnerNameRepository {
    OwnerNameView findByOwnerId(String ownerId);
}