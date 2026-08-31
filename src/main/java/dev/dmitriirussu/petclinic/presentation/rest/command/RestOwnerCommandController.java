package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.usecase.OwnerCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.OwnerUpdateUseCase;
import dev.dmitriirussu.petclinic.application.command.model.CreateOwnerCommand;
import dev.dmitriirussu.petclinic.application.command.model.UpdateOwnerCommand;
import dev.dmitriirussu.petclinic.presentation.rest.request.CreateOwnerRequest;
import dev.dmitriirussu.petclinic.presentation.rest.request.UpdateOwnerRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners")
public class RestOwnerCommandController {
    private final OwnerCreateUseCase createOwnerUseCase;
    private final OwnerUpdateUseCase updateOwnerUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOwner(
            @Valid @RequestBody CreateOwnerRequest request,
            HttpServletResponse response
    ) {
        String id = createOwnerUseCase.createOwner(
                new CreateOwnerCommand(
                        request.firstName(),
                        request.lastName(),
                        request.street(),
                        request.city(),
                        request.telephone()
                )
        );
        response.setHeader(HttpHeaders.LOCATION, "/api/owners/" + id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOwner(
            @PathVariable String id,
            @Valid @RequestBody UpdateOwnerRequest request
    ) {
        updateOwnerUseCase.updateOwner(new UpdateOwnerCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.street(),
                request.city(),
                request.telephone()
        ));
    }
}
