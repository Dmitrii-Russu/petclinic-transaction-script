package dev.dmitriirussu.petclinic.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateOwnerRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @Pattern(regexp = "[\\p{L}\\p{M}\\- ]+", message = "First name must contain only letters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @Pattern(regexp = "[\\p{L}\\p{M}\\- ]+", message = "Last name must contain only letters")
        String lastName,

        @NotBlank(message = "Street is required")
        @Size(min = 1, max = 100, message = "Street must be between 1 and 100 characters")
        String street,

        @NotBlank(message = "City is required")
        @Size(min = 1, max = 50, message = "City must be between 1 and 50 characters")
        String city,

        @NotBlank(message = "Telephone is required")
        @Pattern(regexp = "^\\+?[0-9\\s()\\-]{5,19}$", message = "Invalid phone number format")
        String telephone
) {}