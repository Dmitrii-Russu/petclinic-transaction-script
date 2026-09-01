package dev.dmitriirussu.petclinic.presentation.ssr.dto;

import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import dev.dmitriirussu.petclinic.shared.ValidationMessages;

public class OwnerFormDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = ValidationMessages.NAME_REGEX, message = ValidationMessages.NAME_MESSAGE)
    private String lastName;

    @NotBlank(message = "Street is required")
    @Size(min = 1, max = 100, message = "Street must be between 1 and 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 1, max = 50, message = "City must be between 1 and 50 characters")
    private String city;

    @NotBlank(message = "Telephone is required")
    @Pattern(regexp = ValidationMessages.TELEPHONE_REGEX, message = ValidationMessages.TELEPHONE_MESSAGE)
    private String telephone;

    public OwnerFormDto() {}

    public OwnerFormDto(String firstName, String lastName, String street, String city, String telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.telephone = telephone;
    }

    public static OwnerFormDto from(SsrOwnerEditView view) {
        return new OwnerFormDto(
                view.firstName(),
                view.lastName(),
                view.street(),
                view.city(),
                view.telephone()
        );
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getStreet()    { return street; }
    public String getCity()      { return city; }
    public String getTelephone() { return telephone; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}