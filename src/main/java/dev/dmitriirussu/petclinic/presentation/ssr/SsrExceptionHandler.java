package dev.dmitriirussu.petclinic.presentation.ssr;

import dev.dmitriirussu.petclinic.application.query.catalog.SsrPetTypeCatalog;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrVisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.OwnerFormDto;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.PetFormDto;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.VisitFormDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@ControllerAdvice(basePackages = "dev.dmitriirussu.petclinic.presentation.ssr")
public class SsrExceptionHandler {

    private final SsrVisitCreateFormUseCase visitCreateFormUseCase;
    private final SsrPetTypeCatalog catalog;

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound(NoSuchElementException ex, Model model) {
        return renderSearchWithError(ex.getMessage(), model);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public String handleDomainViolation(RuntimeException ex, HttpServletRequest request, Model model) {
        if (request.getAttribute("ownerForm") instanceof OwnerFormDto ownerForm) {
            model.addAttribute("ownerForm", ownerForm);
            model.addAttribute("ownerId", request.getAttribute("ownerId"));
            model.addAttribute("error", ex.getMessage());
            return "owner/form/owner-create-edit-form";
        }

        if (request.getAttribute("petForm") instanceof PetFormDto petForm) {
            model.addAttribute("ownerFirstName", petForm.getOwnerFirstName());
            model.addAttribute("ownerLastName", petForm.getOwnerLastName());
            model.addAttribute("petTypes", catalog.getAllTypes());
            model.addAttribute("petForm", petForm);
            model.addAttribute("petId", request.getAttribute("petId"));
            model.addAttribute("error", ex.getMessage());
            return "owner/form/pet-create-edit-form";
        }

        if (request.getAttribute("visitForm") instanceof VisitFormDto submittedForm) {
            String petId = (String) request.getAttribute("petId");

            model.addAttribute(
                    "visitForm",
                    visitCreateFormUseCase.getVisitCreateFormByPetId(petId)
            );
            model.addAttribute("form", submittedForm);
            model.addAttribute("error", ex.getMessage());

            return "owner/form/visit-create-form";
        }

        return renderSearchWithError(ex.getMessage(), model);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex, Model model) {
        return renderSearchWithError("Invalid page number", model);
    }

    private String renderSearchWithError(String error, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("message", "");
        return "owner/form/main-page-search";
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception ex, Model model) {
        return renderSearchWithError("Something went wrong. Please try again.", model);
    }
}