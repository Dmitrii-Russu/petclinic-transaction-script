package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.SsrPetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.view.pet.SsrPetEditView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
class SsrPetEditFormRepositoryImpl implements SsrPetEditFormRepository {
    private final JdbcClient jdbc;
    private static final String PET_FIND_EDIT_VIEW_SQL =
            SqlLoader.load("sql/query/ssr-pet-edit-form.sql");

    @Override
    @Cacheable(cacheNames = "petEditForm", key = "#petId")
    public SsrPetEditView getPetEditFormById(String petId) {

        return jdbc.sql(PET_FIND_EDIT_VIEW_SQL)
                .param("id", petId)
                .query(new DataClassRowMapper<>(SsrPetEditView.class))
                .optional()
                .orElseThrow(() -> new NoSuchElementException("Pet not found: " + petId));
    }
}
