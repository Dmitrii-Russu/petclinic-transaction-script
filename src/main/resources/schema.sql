DROP TABLE IF EXISTS vet_specialties;
DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS owners;
DROP TABLE IF EXISTS pet_types;
DROP TABLE IF EXISTS specialties;
DROP TABLE IF EXISTS vets;

CREATE TABLE pet_types (
    name VARCHAR(50) CONSTRAINT pk_pet_type PRIMARY KEY
);

CREATE TABLE owners (
    id         TEXT  CONSTRAINT pk_owner PRIMARY KEY,
    first_name TEXT  NOT NULL CONSTRAINT chk_owner_first_name_length CHECK (length(first_name) BETWEEN 2 AND 50),
    last_name  TEXT  NOT NULL CONSTRAINT chk_owner_last_name_length CHECK (length(last_name) BETWEEN 2 AND 50),
    street     TEXT NOT NULL CONSTRAINT chk_owner_street_length CHECK (length(street) BETWEEN 1 AND 100),
    city       TEXT  NOT NULL CONSTRAINT chk_owner_city_length CHECK (length(city) BETWEEN 1 AND 50),
    telephone  TEXT  NOT NULL CONSTRAINT uq_owner_telephone UNIQUE
);

CREATE TABLE pets (
    id         TEXT CONSTRAINT pk_pet PRIMARY KEY,
    name       TEXT NOT NULL CONSTRAINT chk_pet_name_length CHECK (length(name) BETWEEN 1 AND 30),
    birth_date DATE        NOT NULL CONSTRAINT chk_pet_birth_date_not_future CHECK (birth_date <= CURRENT_DATE),
    type       TEXT NOT NULL CONSTRAINT fk_pet_type REFERENCES pet_types(name),
    owner_id   TEXT NOT NULL CONSTRAINT fk_pet_owner REFERENCES owners(id) ON DELETE CASCADE,
    CONSTRAINT uq_pet_identity UNIQUE (owner_id, name, birth_date, type)
);

CREATE TABLE visits (
    id          TEXT  CONSTRAINT pk_visit PRIMARY KEY,
    visit_date  DATE         NOT NULL,
    description TEXT NOT NULL CONSTRAINT chk_visit_description_length CHECK (length(description) BETWEEN 1 AND 255),
    pet_id      TEXT  NOT NULL CONSTRAINT fk_visit_pet REFERENCES pets(id) ON DELETE CASCADE,
    CONSTRAINT uq_visit_pet_date UNIQUE (pet_id, visit_date)
);

CREATE TABLE specialties (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE vets (
    id         VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE vet_specialties (
    vet_id         VARCHAR(36) NOT NULL REFERENCES vets(id) ON DELETE CASCADE,
    specialty_name VARCHAR(50) NOT NULL REFERENCES specialties(name),
    PRIMARY KEY (vet_id, specialty_name)
);