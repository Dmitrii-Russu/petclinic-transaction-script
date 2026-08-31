package dev.dmitriirussu.petclinic.application.query.view.owner;

import java.util.List;

public record OwnerListView(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone,
        List<String> pets
) {
    public OwnerListView { pets = List.copyOf(pets); }
}
