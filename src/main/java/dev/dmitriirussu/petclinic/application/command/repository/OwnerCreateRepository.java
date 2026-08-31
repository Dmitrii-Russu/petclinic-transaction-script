package dev.dmitriirussu.petclinic.application.command.repository;

import dev.dmitriirussu.petclinic.model.Owner;

public interface OwnerCreateRepository {
    void insert(Owner owner);
}
