package dev.dmitriirussu.petclinic.presentation.rest.command;

import dev.dmitriirussu.petclinic.application.command.model.OwnerCreateCommand;
import dev.dmitriirussu.petclinic.application.command.model.OwnerUpdateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.OwnerCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.OwnerUpdateUseCase;
import dev.dmitriirussu.petclinic.presentation.rest.request.OwnerRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/owners")
@RestController("restOwnerCommandController")
public class OwnerCommandController {
    private final OwnerCreateUseCase createOwnerUseCase;
    private final OwnerUpdateUseCase updateOwnerUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOwner(
            @Valid @RequestBody OwnerRequest request,
            HttpServletResponse response
    ) {
        String ownerId = createOwnerUseCase.createOwner(
                new OwnerCreateCommand(
                        request.firstName(),
                        request.lastName(),
                        request.street(),
                        request.city(),
                        request.telephone()
                )
        );
        response.setHeader(HttpHeaders.LOCATION, "/api/owners/" + ownerId);
    }

    @PutMapping("/{ownerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOwner(
            @PathVariable String ownerId,
            @Valid @RequestBody OwnerRequest request
    ) {
        updateOwnerUseCase.updateOwner(new OwnerUpdateCommand(
                ownerId,
                request.firstName(),
                request.lastName(),
                request.street(),
                request.city(),
                request.telephone()
        ));
    }
}
