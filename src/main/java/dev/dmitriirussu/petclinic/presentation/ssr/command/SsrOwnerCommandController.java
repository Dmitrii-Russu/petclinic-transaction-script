package dev.dmitriirussu.petclinic.presentation.ssr.command;

import dev.dmitriirussu.petclinic.application.command.usecase.OwnerCreateUseCase;
import dev.dmitriirussu.petclinic.application.command.usecase.OwnerUpdateUseCase;
import dev.dmitriirussu.petclinic.application.command.model.CreateOwnerCommand;
import dev.dmitriirussu.petclinic.application.command.model.UpdateOwnerCommand;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.OwnerFormDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owners")
public class SsrOwnerCommandController {
    private final OwnerCreateUseCase createOwnerUseCase;
    private final OwnerUpdateUseCase updateOwnerUseCase;

    @PostMapping("/new")
    public String createOwner(
            @Valid @ModelAttribute OwnerFormDto ownerForm,
            BindingResult binding,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("ownerForm", ownerForm);
            model.addAttribute("ownerId", null);
            model.addAttribute("error", errorMessage(binding));
            return "owner/form/owner-create-edit-form";
        }
        request.setAttribute("ownerForm", ownerForm);
        request.setAttribute("ownerId", null);
        String id = createOwnerUseCase.createOwner(new CreateOwnerCommand(
                ownerForm.getFirstName(), ownerForm.getLastName(),
                ownerForm.getStreet(), ownerForm.getCity(), ownerForm.getTelephone()
        ));
        redirectAttributes.addFlashAttribute("message", "Owner created successfully");
        return "redirect:/owners/" + id;
    }

    @PostMapping("/{id}/edit")
    public String updateOwner(
            @PathVariable String id,
            @Valid @ModelAttribute OwnerFormDto ownerForm,
            BindingResult binding,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("ownerForm", ownerForm);
            model.addAttribute("ownerId", id);
            model.addAttribute("error", errorMessage(binding));
            return "owner/form/owner-create-edit-form";
        }
        request.setAttribute("ownerForm", ownerForm);
        request.setAttribute("ownerId", id);
        updateOwnerUseCase.updateOwner(new UpdateOwnerCommand(
                id, ownerForm.getFirstName(), ownerForm.getLastName(),
                ownerForm.getStreet(), ownerForm.getCity(), ownerForm.getTelephone()
        ));
        redirectAttributes.addFlashAttribute("message", "Owner updated successfully");
        return "redirect:/owners/" + id;
    }

    private static String errorMessage(BindingResult binding) {
        return binding.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}