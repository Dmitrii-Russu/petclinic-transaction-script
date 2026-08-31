package dev.dmitriirussu.petclinic.presentation.rest.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateVisitRequest(
        @NotBlank(message = "Pet is required")
        String petId,

        @NotNull(message = "Date is required")
        @FutureOrPresent(message = "Visit date must be today or in the future")
        LocalDate date,

        @NotBlank(message = "Description is required")
        @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
        String description,

        @NotBlank(message = "Owner id is required")
        String ownerId
) {}