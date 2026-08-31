package dev.dmitriirussu.petclinic.shared.pagination;

public record OwnerSearchCriteria(String lastNamePrefix) {

    public OwnerSearchCriteria {
        if (lastNamePrefix != null) {
            lastNamePrefix = lastNamePrefix.strip();
        }
    }

    public boolean isEmpty() {
        return lastNamePrefix == null || lastNamePrefix.isBlank();
    }
}
