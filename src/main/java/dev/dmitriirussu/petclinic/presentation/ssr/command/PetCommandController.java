package dev.dmitriirussu.petclinic.presentation.ssr.command;

import dev.dmitriirussu.petclinic.application.command.model.PetCreateCommand;
import dev.dmitriirussu.petclinic.application.command.model.PetUpdateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.PetCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.PetUpdateUseCase;
import dev.dmitriirussu.petclinic.application.query.catalog.SsrPetTypeCatalog;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.PetFormDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/pets")
@Controller("ssrPetCommandController")
public class PetCommandController {
    private final PetCreateUseCase createPetUseCase;
    private final PetUpdateUseCase updatePetUseCase;
    private final SsrPetTypeCatalog catalog;

    @PostMapping("/new")
    public String createPet(
            @Valid @ModelAttribute PetFormDto petForm,
            BindingResult binding,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("ownerFirstName", petForm.getOwnerFirstName());
            model.addAttribute("ownerLastName", petForm.getOwnerLastName());
            model.addAttribute("petTypes", catalog.getAllTypes());
            model.addAttribute("petForm", petForm);
            model.addAttribute("petId", null);
            model.addAttribute("error", errorMessage(binding));
            return "owner/form/pet-create-edit-form";
        }
        request.setAttribute("petForm", petForm);
        request.setAttribute("petId", null);
        createPetUseCase.createPet(new PetCreateCommand(
                petForm.getName(), petForm.getBirthDate(),
                petForm.getType(), petForm.getOwnerId()
        ));
        redirectAttributes.addFlashAttribute("message", "Pet saved successfully");
        return "redirect:/owners/" + petForm.getOwnerId();
    }

    @PostMapping("/{petId}/edit")
    public String updatePet(
            @PathVariable String petId,
            @Valid @ModelAttribute PetFormDto petForm,
            BindingResult binding,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("ownerFirstName", petForm.getOwnerFirstName());
            model.addAttribute("ownerLastName", petForm.getOwnerLastName());
            model.addAttribute("petTypes", catalog.getAllTypes());
            model.addAttribute("petForm", petForm);
            model.addAttribute("petId", petId);
            model.addAttribute("error", errorMessage(binding));
            return "owner/form/pet-create-edit-form";
        }
        request.setAttribute("petForm", petForm);
        request.setAttribute("petId", petId);
        updatePetUseCase.updatePet(new PetUpdateCommand(
                petId, petForm.getName(), petForm.getBirthDate(),
                petForm.getType(), petForm.getOwnerId()
        ));
        redirectAttributes.addFlashAttribute("message", "Pet saved successfully");
        return "redirect:/owners/" + petForm.getOwnerId();
    }

    private static String errorMessage(BindingResult binding) {
        return binding.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}