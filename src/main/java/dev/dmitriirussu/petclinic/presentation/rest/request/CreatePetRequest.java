package dev.dmitriirussu.petclinic.presentation.rest.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreatePetRequest(
        @NotBlank(message = "Owner is required")
        String ownerId,

        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
        @Pattern(regexp = "[\\p{L}\\p{M}\\- ]+", message = "Name must contain only letters")
        String name,

        @NotNull(message = "Birth date is required")
        @PastOrPresent(message = "Birth date must be in the past or present")
        LocalDate birthDate,

        @NotBlank(message = "Type is required")
        @Size(min = 1, max = 30, message = "Type must be between 1 and 30 characters")
        @Pattern(regexp = "Dog|Cat|Bird|Rabbit|Hamster", message = "Invalid pet type")
        String type
) {}