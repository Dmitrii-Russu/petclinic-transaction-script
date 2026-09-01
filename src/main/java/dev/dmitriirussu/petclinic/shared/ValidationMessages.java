package dev.dmitriirussu.petclinic.shared;

public final class ValidationMessages {
    private ValidationMessages() {}

    public static final String NAME_REGEX = "[\\p{L}\\p{M}\\- ]+";
    public static final String NAME_MESSAGE =
            "Name must contain only letters, spaces, or hyphens";

    public static final String BIRTH_DATE_MESSAGE =
            "Birth date must be in the past or present";

    public static final String VISIT_DATE_MESSAGE =
            "Visit date must be today or in the future";

    public static final String TELEPHONE_REGEX = "^\\+?[0-9\\s()\\-]{5,20}$";
    public static final String TELEPHONE_MESSAGE = "Invalid phone number format";
}
