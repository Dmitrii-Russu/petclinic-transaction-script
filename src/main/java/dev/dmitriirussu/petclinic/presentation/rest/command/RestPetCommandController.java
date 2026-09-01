package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.usecase.PetCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.PetUpdateUseCase;
import dev.dmitriirussu.petclinic.application.command.model.CreatePetCommand;
import dev.dmitriirussu.petclinic.application.command.model.UpdatePetCommand;
import dev.dmitriirussu.petclinic.presentation.rest.request.PetRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class RestPetCommandController {

    private final PetCreateUseCase createPetUseCase;
    private final PetUpdateUseCase updatePetUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPet(
            @Valid @RequestBody PetRequest request,
            HttpServletResponse response
    ) {
        String id = createPetUseCase.createPet(
                new CreatePetCommand(
                        request.name(),
                        request.birthDate(),
                        request.type(),
                        request.ownerId()
                )
        );
        response.setHeader(HttpHeaders.LOCATION, "/api/pets/" + id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePet(
            @PathVariable String id,
            @Valid @RequestBody PetRequest request
    ) {
        updatePetUseCase.updatePet(new UpdatePetCommand(
                id,
                request.name(),
                request.birthDate(),
                request.type(),
                request.ownerId()
        ));
    }
}