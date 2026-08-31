package dev.dmitriirussu.petclinic.application.query.repository;

import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;

public interface SsrVisitCreateFormRepository {
    SsrVisitCreateView getVisitCreateFormByPetId(String petId);
}