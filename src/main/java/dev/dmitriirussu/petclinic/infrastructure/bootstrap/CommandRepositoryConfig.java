package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.infrastructure.command.AllCommandRepositoryFactory;
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
        return AllCommandRepositoryFactory.ownerCreateRepository(jdbc, cacheManager);
    }

    @Bean
    OwnerUpdateRepository ownerUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
            ) {
        return AllCommandRepositoryFactory.ownerUpdateRepository(jdbc, cacheManager);
    }

    @Bean
    PetCreateRepository petCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return AllCommandRepositoryFactory.petCreateRepository(jdbc, cacheManager);
    }

    @Bean
    PetUpdateRepository petUpdateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return AllCommandRepositoryFactory.petUpdateRepository(jdbc, cacheManager);
    }

    @Bean
    VisitCreateRepository visitCreateRepository(
            JdbcClient jdbc,
            CacheManager cacheManager
    ) {
        return AllCommandRepositoryFactory.visitCreateRepository(jdbc, cacheManager);
    }
}