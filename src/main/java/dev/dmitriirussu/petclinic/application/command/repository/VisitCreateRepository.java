package dev.dmitriirussu.petclinic.application.command.repository;

import dev.dmitriirussu.petclinic.model.Visit;

public interface VisitCreateRepository {
    void insert(Visit visit, String ownerId);
}
