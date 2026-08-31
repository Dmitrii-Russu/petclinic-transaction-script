package dev.dmitriirussu.petclinic.application.command.repository;

import dev.dmitriirussu.petclinic.model.Owner;

public interface OwnerUpdateRepository {
    void update(Owner owner);
}
