package dev.dmitriirussu.petclinic.application.query.view.owner.ssr;

public record OwnerEditView(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone
) {}
