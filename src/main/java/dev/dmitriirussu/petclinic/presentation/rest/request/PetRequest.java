package dev.dmitriirussu.petclinic.presentation.rest.request;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import dev.dmitriirussu.petclinic.shared.ValidationMessages;

public record PetRequest(
        @NotBlank(message = "Owner is required")
        String ownerId,

        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
        @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
        String name,

        @NotNull(message = "Birth date is required")
        @PastOrPresent(message = ValidationMessages.BIRTH_DATE_MESSAGE)
        LocalDate birthDate,

        @NotBlank(message = "Type is required")
        String type
) {}