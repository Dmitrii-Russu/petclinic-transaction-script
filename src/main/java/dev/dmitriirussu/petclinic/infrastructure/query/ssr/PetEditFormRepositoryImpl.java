package dev.dmitriirussu.petclinic.infrastructure.query.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.pet.ssr.PetEditView;
import dev.dmitriirussu.petclinic.infrastructure.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class PetEditFormRepositoryImpl implements PetEditFormRepository {

    private final JdbcClient jdbc;
    private static final String PET_EDIT_FORM_SQL =
            SqlLoader.load("sql/query/ssr-pet-edit-form.sql");

    @Override
    @Cacheable(cacheNames = "petEditForm", key = "#petId")
    public PetEditView findByPetId(String petId) {

        return jdbc.sql(PET_EDIT_FORM_SQL)
                .param("id", petId)
                .query(new DataClassRowMapper<>(PetEditView.class))
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Pet not found: " + petId));
    }
}
