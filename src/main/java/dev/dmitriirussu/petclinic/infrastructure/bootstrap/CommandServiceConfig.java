package dev.dmitriirussu.petclinic.infrastructure.bootstrap;

import dev.dmitriirussu.petclinic.application.command.repository.*;
import dev.dmitriirussu.petclinic.application.command.service.CommandServiceFactory;
import dev.dmitriirussu.petclinic.application.command.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CommandServiceConfig {

    @Bean
    OwnerCreateUseCase ownerCreateUseCase(OwnerCreateRepository createRepository) {
        return CommandServiceFactory.ownerCreateUseCase(createRepository);
    }

    @Bean
    OwnerUpdateUseCase ownerUpdateUseCase(OwnerUpdateRepository updateRepository) {
        return CommandServiceFactory.ownerUpdateUseCase(updateRepository);
    }

    @Bean
    PetCreateUseCase petCreateUseCase(PetCreateRepository createRepository) {
        return CommandServiceFactory.petCreateUseCase(createRepository);
    }

    @Bean
    PetUpdateUseCase petUpdateUseCase(PetUpdateRepository updateRepository) {
        return CommandServiceFactory.petUpdateUseCase(updateRepository);
    }

    @Bean
    VisitCreateUseCase visitCreateUseCase(VisitCreateRepository createRepository) {
        return CommandServiceFactory.visitCreateUseCase(createRepository);
    }
}
