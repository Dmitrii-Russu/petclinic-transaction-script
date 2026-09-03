package dev.dmitriirussu.petclinic.application.query;

public record OwnerSearchCriteria(String lastNamePrefix) {
    private static final String ESCAPE_CHAR = "\\";

    public OwnerSearchCriteria {
        if (lastNamePrefix != null) {
            lastNamePrefix = escapeLike(lastNamePrefix.strip());
        }
    }

    public boolean isEmpty() {
        return lastNamePrefix == null || lastNamePrefix.isBlank();
    }

    private static String escapeLike(String value) {
        /*
         * Order matters: escape the escape char itself first,
         * otherwise the next two replace() calls double-escape it.
         */
        return value
                .replace(ESCAPE_CHAR, ESCAPE_CHAR + ESCAPE_CHAR)
                .replace("%", ESCAPE_CHAR + "%")
                .replace("_", ESCAPE_CHAR + "_");
    }
}
