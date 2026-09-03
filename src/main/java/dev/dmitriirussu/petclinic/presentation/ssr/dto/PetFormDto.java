package dev.dmitriirussu.petclinic.presentation.ssr.dto;

import dev.dmitriirussu.petclinic.application.query.view.pet.ssr.PetEditView;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import dev.dmitriirussu.petclinic.shared.ValidationMessages;

public class PetFormDto {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
    private String name;

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = ValidationMessages.BIRTH_DATE_MESSAGE)
    private LocalDate birthDate;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Owner is required")
    private String ownerId;

    @NotBlank(message = "Owner first name is required")
    @Size(min = 2, max = 50, message = "Owner first name must be between 2 and 50 characters")
    @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
    private String ownerFirstName;

    @NotBlank(message = "Owner last name is required")
    @Size(min = 2, max = 50, message = "Owner last name must be between 2 and 50 characters")
    @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
    private String ownerLastName;

    public PetFormDto() {}

    public static PetFormDto from(PetEditView view) {
        PetFormDto dto = new PetFormDto();
        dto.name           = view.name();
        dto.birthDate      = view.birthDate();
        dto.type           = view.type();
        dto.ownerId        = view.ownerId();
        dto.ownerFirstName = view.firstName();
        dto.ownerLastName  = view.lastName();
        return dto;
    }

    public String getName()      { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getType()      { return type; }
    public String getOwnerId()   { return ownerId; }
    public String getOwnerFirstName() { return ownerFirstName; }
    public String getOwnerLastName()  { return ownerLastName; }

    public void setName(String name)           { this.name      = name; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setType(String type)           { this.type      = type; }
    public void setOwnerId(String ownerId)     { this.ownerId   = ownerId; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }
    public void setOwnerLastName(String ownerLastName)   { this.ownerLastName  = ownerLastName; }
}