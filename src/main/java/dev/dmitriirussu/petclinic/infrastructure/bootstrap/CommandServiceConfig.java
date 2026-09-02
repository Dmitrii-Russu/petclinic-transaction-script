package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.application.command.service.AllCommandServiceFactory;
import dev.dmitriirussu.petclinic.application.command.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CommandServiceConfig {

    @Bean
    OwnerCreateUseCase createOwnerUseCase(OwnerCreateRepository createRepository) {
        return AllCommandServiceFactory.createOwnerUseCase(createRepository);
    }

    @Bean
    OwnerUpdateUseCase updateOwnerUseCase(OwnerUpdateRepository updateRepository) {
        return AllCommandServiceFactory.updateOwnerUseCase(updateRepository);
    }

    @Bean
    PetCreateUseCase createPetUseCase(PetCreateRepository createRepository) {
        return AllCommandServiceFactory.createPetUseCase(createRepository);
    }

    @Bean
    PetUpdateUseCase updatePetUseCase(PetUpdateRepository updateRepository) {
        return AllCommandServiceFactory.updatePetUseCase(updateRepository);
    }

    @Bean
    VisitCreateUseCase createVisitUseCase(VisitCreateRepository createRepository) {
        return AllCommandServiceFactory.createVisitUseCase(createRepository);
    }
}
