package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class AllQueryRepositoryFactory {

    private AllQueryRepositoryFactory() {}

    // --- SSR-only (forms/detail pages, rendered by JTE controllers) ---

    public static SsrOwnerEditFormRepository ssrOwnerEditFormRepository(JdbcClient jdbc) {
        return new SsrOwnerEditFormRepositoryImpl(jdbc);
    }

    public static SsrOwnerNameRepository ssrOwnerNameRepository(JdbcClient jdbc) {
        return new SsrOwnerNameRepositoryImpl(jdbc);
    }

    public static SsrPetEditFormRepository ssrPetEditFormRepository(JdbcClient jdbc) {
        return new SsrPetEditFormRepositoryImpl(jdbc);
    }

    public static SsrVisitCreateFormRepository ssrVisitCreateFormRepository(JdbcClient jdbc) {
        return new SsrVisitCreateFormRepositoryImpl(jdbc);
    }

    // --- Shared / REST (can also be reused by SSR) ---

    public static FindOwnerRepository findOwnerRepository(JdbcClient jdbc) {
        return new FindOwnerRepositoryImpl(jdbc);
    }

    public static FindOwnerListRepository findOwnerListRepository(JdbcClient jdbc) {
        return new FindOwnerListRepositoryImpl(jdbc);
    }
}