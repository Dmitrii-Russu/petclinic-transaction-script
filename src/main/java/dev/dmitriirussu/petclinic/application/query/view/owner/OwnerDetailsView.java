package dev.dmitriirussu.petclinic.application.query.view.owner;

import dev.dmitriirussu.petclinic.application.query.view.pet.PetDetailsView;

import java.util.List;

public record OwnerDetailsView(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone,
        List<PetDetailsView> pets
) {
    public OwnerDetailsView { pets = List.copyOf(pets); }
}
