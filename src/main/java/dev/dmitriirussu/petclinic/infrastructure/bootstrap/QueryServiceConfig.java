package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.OwnerNameRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.PetEditFormRepository;
import dev.dmitriirussu.petclinic.application.query.repository.ssr.VisitCreateFormRepository;
import dev.dmitriirussu.petclinic.application.query.service.QueryServiceFactory;
import dev.dmitriirussu.petclinic.application.query.service.ssr.SsrQueryServiceFactory;
import dev.dmitriirussu.petclinic.application.query.usecase.*;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.OwnerNameUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.PetEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.ssr.VisitCreateFormUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QueryServiceConfig {

    @Bean
    OwnerFindUseCase ownerFindUseCase(OwnerFindRepository repository) {
        return QueryServiceFactory.ownerFindUseCase(repository);
    }

    @Bean
    OwnerSearchUseCase ownerSearchUseCase(OwnerSearchRepository repository) {
        return QueryServiceFactory.ownerSearchUseCase(repository);
    }

    @Bean
    OwnerEditFormUseCase ssrOwnerEditFormUseCase(OwnerEditFormRepository repository) {
        return SsrQueryServiceFactory.ownerEditFormUseCase(repository);
    }

    @Bean
    OwnerNameUseCase ssrOwnerNameUseCase(OwnerNameRepository repository) {
        return SsrQueryServiceFactory.ownerNameUseCase(repository);
    }

    @Bean
    PetEditFormUseCase ssrPetEditFormUseCase(PetEditFormRepository repository) {
        return SsrQueryServiceFactory.petEditFormUseCase(repository);
    }

    @Bean
    VisitCreateFormUseCase ssrVisitCreateFormUseCase(VisitCreateFormRepository repository) {
        return SsrQueryServiceFactory.visitCreateFormUseCase(repository);
    }
}