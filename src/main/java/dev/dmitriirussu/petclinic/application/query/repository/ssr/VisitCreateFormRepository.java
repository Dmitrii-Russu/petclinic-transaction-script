package dev.dmitriirussu.petclinic.application.query.repository.ssr;

import dev.dmitriirussu.petclinic.application.query.view.visit.ssr.VisitCreateView;

public interface VisitCreateFormRepository {
    VisitCreateView findByPetId(String petId);
}