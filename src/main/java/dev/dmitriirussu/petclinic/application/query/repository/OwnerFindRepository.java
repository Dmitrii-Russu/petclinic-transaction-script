package dev.dmitriirussu.petclinic.application.query.repository;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;

public interface OwnerFindRepository {
    OwnerDetailsView findById(String id);
}
