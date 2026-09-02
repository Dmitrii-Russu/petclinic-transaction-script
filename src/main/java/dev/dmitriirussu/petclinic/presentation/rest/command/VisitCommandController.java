package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.model.VisitCreateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.VisitCreateUseCase;
import dev.dmitriirussu.petclinic.presentation.rest.request.VisitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/visits")
@RestController("restVisitCommandController")
public class VisitCommandController {
    private final VisitCreateUseCase createVisitUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVisit(@Valid @RequestBody VisitRequest request) {

        createVisitUseCase.createVisit(
                new VisitCreateCommand(
                        request.visitDate(),
                        request.description(),
                        request.petId(),
                        request.ownerId()
                )
        );
    }
}