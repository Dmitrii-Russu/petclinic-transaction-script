package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.infrastructure.command.CommandRepositoryFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
class CommandRepositoryConfig {

    @Bean
    OwnerCreateRepository ownerCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return CommandRepositoryFactory.ownerCreateRepository(jdbc, cacheManager);
    }

    @Bean
    OwnerUpdateRepository ownerUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
            ) {
        return CommandRepositoryFactory.ownerUpdateRepository(jdbc, cacheManager);
    }

    @Bean
    PetCreateRepository petCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return CommandRepositoryFactory.petCreateRepository(jdbc, cacheManager);
    }

    @Bean
    PetUpdateRepository petUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return CommandRepositoryFactory.petUpdateRepository(jdbc, cacheManager);
    }

    @Bean
    VisitCreateRepository visitCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return CommandRepositoryFactory.visitCreateRepository(jdbc, cacheManager);
    }
}