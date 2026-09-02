package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.model.PetCreateCommand;
import dev.dmitriirussu.petclinic.application.command.model.PetUpdateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.PetCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.PetUpdateUseCase;
import dev.dmitriirussu.petclinic.presentation.rest.request.PetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/pets")
@RestController("restPetCommandController")
public class PetCommandController {

    private final PetCreateUseCase createPetUseCase;
    private final PetUpdateUseCase updatePetUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPet(@Valid @RequestBody PetRequest request) {

        createPetUseCase.createPet(
                new PetCreateCommand(
                        request.name(),
                        request.birthDate(),
                        request.type(),
                        request.ownerId()
                )
        );
    }

    @PutMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePet(
            @PathVariable String petId,
            @Valid @RequestBody PetRequest request
    ) {
        updatePetUseCase.updatePet(new PetUpdateCommand(
                petId,
                request.name(),
                request.birthDate(),
                request.type(),
                request.ownerId()
        ));
    }
}