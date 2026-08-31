package dev.dmitriirussu.petclinic.application.command.repository;

import dev.dmitriirussu.petclinic.model.Pet;

public interface PetUpdateRepository {
    void update(Pet pet);
}
