package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.query.repository.*;
import dev.dmitriirussu.petclinic.application.query.service.AllQueryServiceFactory;
import dev.dmitriirussu.petclinic.application.query.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QueryServiceConfig {

    @Bean
    FindOwnerUseCase findOwnerUseCase(FindOwnerRepository repository) {
        return AllQueryServiceFactory.findOwnerUseCase(repository);
    }

    @Bean
    FindOwnerListUseCase findOwnerListUseCase(FindOwnerListRepository repository) {
        return AllQueryServiceFactory.findOwnerListUseCase(repository);
    }

    @Bean
    SsrOwnerEditFormUseCase ssrOwnerEditFormUseCase(SsrOwnerEditFormRepository repository) {
        return AllQueryServiceFactory.ssrOwnerEditFormUseCase(repository);
    }

    @Bean
    SsrOwnerNameUseCase ssrOwnerNameUseCase(SsrOwnerNameRepository repository) {
        return AllQueryServiceFactory.ssrOwnerNameUseCase(repository);
    }

    @Bean
    SsrPetEditFormUseCase ssrPetEditFormUseCase(SsrPetEditFormRepository repository) {
        return AllQueryServiceFactory.ssrPetEditFormUseCase(repository);
    }

    @Bean
    SsrVisitCreateFormUseCase ssrVisitCreateFormUseCase(SsrVisitCreateFormRepository repository) {
        return AllQueryServiceFactory.ssrVisitCreateFormUseCase(repository);
    }
}