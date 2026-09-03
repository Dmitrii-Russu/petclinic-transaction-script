package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class QueryRepositoryFactory {

    private QueryRepositoryFactory() {}

    public static OwnerFindRepository ownerFindRepository(JdbcClient jdbc) {
        return new OwnerFindRepositoryImpl(jdbc);
    }

    public static OwnerSearchRepository ownerSearchRepository(JdbcClient jdbc) {
        return new OwnerSearchRepositoryImpl(jdbc);
    }
}