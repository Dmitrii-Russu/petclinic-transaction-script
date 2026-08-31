package dev.dmitriirussu.petclinic.application.command.model;

public record UpdateOwnerCommand(
        String id,
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone
) {}
