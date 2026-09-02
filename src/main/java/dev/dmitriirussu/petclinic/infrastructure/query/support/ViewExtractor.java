package dev.dmitriirussu.petclinic.infrastructure.query.support;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.application.query.view.pet.PetDetailsView;
import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;
import dev.dmitriirussu.petclinic.application.query.view.visit.VisitView;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public final class ViewExtractor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static OwnerDetailsView getOwnerDetails(ResultSet rs, int rowNum) throws SQLException {
        try {
            List<PetDetailsView> pets = objectMapper.readValue(
                    rs.getString("pets_json"),
                    new TypeReference<List<PetDetailsView>>() {}
            );
            return new OwnerDetailsView(
                    rs.getString("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getString("telephone"),
                    pets
            );
        } catch (JacksonException e) {
            throw new SQLException("Failed to parse pets_json for owner " + rs.getString("id"), e);
        }
    }

    public static OwnerListView getOwnerWithPetNames(ResultSet rs, int rowNum) throws SQLException {
        String id        = rs.getString("id");
        String firstName = rs.getString("first_name");
        String lastName  = rs.getString("last_name");
        String street    = rs.getString("street");
        String city      = rs.getString("city");
        String telephone = rs.getString("telephone");

        Array sqlArray = rs.getArray("pet_names");
        List<String> petNames = sqlArray == null
                ? List.of()
                : Arrays.asList((String[]) sqlArray.getArray());

        return new OwnerListView(id, firstName, lastName, street, city, telephone, petNames);
    }

    public static SsrVisitCreateView getVisitCreateForm(ResultSet rs, int rowNum) throws SQLException {
        try {
            List<VisitView> visits = objectMapper.readValue(
                    rs.getString("visits_json"),
                    new TypeReference<List<VisitView>>() {}
            );
            return new SsrVisitCreateView(
                    rs.getString("pet_id"),
                    rs.getString("pet_name"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getString("type"),
                    rs.getString("owner_id"),
                    rs.getString("owner_first_name"),
                    rs.getString("owner_last_name"),
                    visits
            );
        } catch (JacksonException e) {
            throw new SQLException("Failed to parse visits_json for pet " + rs.getString("pet_id"), e);
        }
    }
}
