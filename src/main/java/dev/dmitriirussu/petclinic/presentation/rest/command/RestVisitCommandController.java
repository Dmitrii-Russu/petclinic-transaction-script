package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.usecase.VisitCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.model.CreateVisitCommand;
import dev.dmitriirussu.petclinic.presentation.rest.request.CreateVisitRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visits")
public class RestVisitCommandController {
    private final VisitCreateUseCase createVisitUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVisit(
            @Valid @RequestBody CreateVisitRequest request,
            HttpServletResponse response
    ) {
        String id = createVisitUseCase.createVisit(
                new CreateVisitCommand(
                        request.date(),
                        request.description(),
                        request.petId(),
                        request.ownerId()
                )
        );
        response.setHeader(HttpHeaders.LOCATION, "/api/visits/" + id);
    }
}