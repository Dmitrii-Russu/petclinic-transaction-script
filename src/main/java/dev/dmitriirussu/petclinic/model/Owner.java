package dev.dmitriirussu.petclinic.model;

public record Owner(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone
) {}