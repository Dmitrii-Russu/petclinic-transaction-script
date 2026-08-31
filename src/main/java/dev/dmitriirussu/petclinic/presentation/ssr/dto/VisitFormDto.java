package dev.dmitriirussu.petclinic.presentation.ssr.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class VisitFormDto {

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Visit date must be a valid date, today or in the future")
    private LocalDate date;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    public VisitFormDto() {}

    public LocalDate getDate()        { return date; }
    public String getDescription()    { return description; }

    public void setDate(LocalDate date)             { this.date = date; }
    public void setDescription(String description)  { this.description = description; }
}