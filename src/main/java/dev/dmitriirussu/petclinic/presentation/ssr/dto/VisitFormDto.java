package dev.dmitriirussu.petclinic.presentation.ssr.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import dev.dmitriirussu.petclinic.shared.ValidationMessages;

public class VisitFormDto {

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = ValidationMessages.VISIT_DATE_MESSAGE)
    private LocalDate visitDate;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    public VisitFormDto() {}

    public LocalDate getDate()        { return visitDate; }
    public String getDescription()    { return description; }

    public void setDate(LocalDate date)             { this.visitDate = date; }
    public void setDescription(String description)  { this.description = description; }
}