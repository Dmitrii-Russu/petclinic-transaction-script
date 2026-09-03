package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.query.catalog.ssr.PetTypeCatalog;
import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import dev.dmitriirussu.petclinic.infrastructure.query.QueryRepositoryFactory;
import dev.dmitriirussu.petclinic.infrastructure.query.catalog.ssr.PetTypeCatalogFactory;
import dev.dmitriirussu.petclinic.infrastructure.query.ssr.SsrQueryRepositoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
class QueryRepositoryConfig {

    @Bean
    OwnerFindRepository ownerFindRepository(JdbcClient jdbc) {
        return QueryRepositoryFactory.ownerFindRepository(jdbc);
    }

    @Bean
    OwnerSearchRepository ownerSearchRepository(JdbcClient jdbc) {
        return QueryRepositoryFactory.ownerSearchRepository(jdbc);
    }

    @Bean
    OwnerEditFormRepository ssrOwnerEditFormRepository(JdbcClient jdbc) {
        return SsrQueryRepositoryFactory.ownerEditFormRepository(jdbc);
    }

    @Bean
    OwnerNameRepository ssrOwnerNameRepository(JdbcClient jdbc) {
        return SsrQueryRepositoryFactory.ownerNameRepository(jdbc);
    }

    @Bean
    PetEditFormRepository ssrPetEditFormRepository(JdbcClient jdbc) {
        return SsrQueryRepositoryFactory.petEditFormRepository(jdbc);
    }

    @Bean
    VisitCreateFormRepository ssrVisitCreateFormRepository(JdbcClient jdbc) {
        return SsrQueryRepositoryFactory.visitCreateFormRepository(jdbc);
    }

    @Bean
    PetTypeCatalog petTypeCatalog(JdbcClient jdbc) {
        return PetTypeCatalogFactory.petTypeCatalog(jdbc);
    }
}