package dev.dmitriirussu.petclinic.presentation.rest.request;

import jakarta.validation.constraints.*;

import dev.dmitriirussu.petclinic.shared.ValidationMessages;

public record OwnerRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
        String lastName,

        @NotBlank(message = "Street is required")
        @Size(min = 1, max = 100, message = "Street must be between 1 and 100 characters")
        String street,

        @NotBlank(message = "City is required")
        @Size(min = 1, max = 50, message = "City must be between 1 and 50 characters")
        String city,

        @NotBlank(message = "Telephone is required")
        @Pattern(regexp = ValidationMessages.TELEPHONE_REGEX, message = ValidationMessages.TELEPHONE_MESSAGE)
        String telephone
) {}