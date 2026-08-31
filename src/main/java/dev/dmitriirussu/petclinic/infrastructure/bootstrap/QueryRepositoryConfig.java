package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.query.catalog.PetTypeCatalog;
import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.infrastructure.query.AllQueryRepositoryFactory;
import dev.dmitriirussu.petclinic.infrastructure.query.catalog.PetTypeCatalogReadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
class QueryRepositoryConfig {

    @Bean
    FindOwnerRepository findOwnerRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.findOwnerRepository(jdbc);
    }

    @Bean
    FindOwnerListRepository findOwnerListRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.findOwnerListRepository(jdbc);
    }

    @Bean
    SsrOwnerEditFormRepository ssrOwnerEditFormRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.ssrOwnerEditFormRepository(jdbc);
    }

    @Bean
    SsrOwnerNameRepository ssrOwnerNameRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.ssrOwnerNameRepository(jdbc);
    }

    @Bean
    SsrPetEditFormRepository ssrPetEditFormRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.ssrPetEditFormRepository(jdbc);
    }

    @Bean
    SsrVisitCreateFormRepository ssrVisitCreateFormRepository(JdbcClient jdbc) {
        return AllQueryRepositoryFactory.ssrVisitCreateFormRepository(jdbc);
    }

    @Bean
    PetTypeCatalog petTypeCatalog(JdbcClient jdbc) {
        return PetTypeCatalogReadFactory.petTypeCatalog(jdbc);
    }
}