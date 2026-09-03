package dev.dmitriirussu.petclinic.infrastructure.query.ssr;

import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class SsrQueryRepositoryFactory {

    private SsrQueryRepositoryFactory() {}

    public static OwnerEditFormRepository ownerEditFormRepository(JdbcClient jdbc) {
        return new OwnerEditFormRepositoryImpl(jdbc);
    }

    public static OwnerNameRepository ownerNameRepository(JdbcClient jdbc) {
        return new OwnerNameRepositoryImpl(jdbc);
    }

    public static PetEditFormRepository petEditFormRepository(JdbcClient jdbc) {
        return new PetEditFormRepositoryImpl(jdbc);
    }

    public static VisitCreateFormRepository visitCreateFormRepository(JdbcClient jdbc) {
        return new VisitCreateFormRepositoryImpl(jdbc);
    }
}
