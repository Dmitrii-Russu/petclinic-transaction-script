package dev.dmitriirussu.petclinic.application.command.model;

public record CreateOwnerCommand(
        String firstName,
        String lastName,
        String street,
        String city,
        String telephone
) {}
