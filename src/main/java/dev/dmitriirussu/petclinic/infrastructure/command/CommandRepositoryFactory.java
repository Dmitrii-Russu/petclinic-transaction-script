package dev.dmitriirussu.petclinic.infrastructure.command;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.simple.JdbcClient;

public final class CommandRepositoryFactory {

    private CommandRepositoryFactory() {}

    // --- Owner ---

    public static OwnerCreateRepository ownerCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return new OwnerCreateRepositoryImpl(jdbc, cacheManager);
    }

    public static OwnerUpdateRepository ownerUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return new OwnerUpdateRepositoryImpl(jdbc, cacheManager);
    }

    // --- Pet ---

    public static PetCreateRepository petCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return new PetCreateRepositoryImpl(jdbc, cacheManager);
    }

    public static PetUpdateRepository petUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return new PetUpdateRepositoryImpl(jdbc, cacheManager);
    }

    // --- Visit ---

    public static VisitCreateRepository visitCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return new VisitCreateRepositoryImpl(jdbc, cacheManager);
    }
}
