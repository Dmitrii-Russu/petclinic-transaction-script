package dev.dmitriirussu.petclinic.application.command.repository;

import dev.dmitriirussu.petclinic.model.Pet;

public interface PetCreateRepository {
    void create(Pet pet);
}
