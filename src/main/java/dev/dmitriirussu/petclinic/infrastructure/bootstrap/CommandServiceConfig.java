package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.application.command.service.AllCommandServiceFactory;
import dev.dmitriirussu.petclinic.application.command.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CommandServiceConfig {

    @Bean
    OwnerCreateUseCase createOwnerUseCase(OwnerCreateRepository insertRepository) {
        return AllCommandServiceFactory.createOwnerUseCase(insertRepository);
    }

    @Bean
    OwnerUpdateUseCase updateOwnerUseCase(OwnerUpdateRepository updateRepository) {
        return AllCommandServiceFactory.updateOwnerUseCase(updateRepository);
    }

    @Bean
    PetCreateUseCase createPetUseCase(PetCreateRepository insertRepository) {
        return AllCommandServiceFactory.createPetUseCase(insertRepository);
    }

    @Bean
    PetUpdateUseCase updatePetUseCase(PetUpdateRepository updateRepository) {
        return AllCommandServiceFactory.updatePetUseCase(updateRepository);
    }

    @Bean
    VisitCreateUseCase createVisitUseCase(VisitCreateRepository insertRepository) {
        return AllCommandServiceFactory.createVisitUseCase(insertRepository);
    }
}
