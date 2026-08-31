package dev.dmitriirussu.petclinic.application.query.view.owner;

public record SsrOwnerEditView(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone
) {}
