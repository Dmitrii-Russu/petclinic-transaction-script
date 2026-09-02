package dev.dmitriirussu.petclinic.presentation.ssr.command;

import dev.dmitriirussu.petclinic.application.command.model.VisitCreateCommand;
import dev.dmitriirussu.petclinic.application.command.usecase.VisitCreateUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrVisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.VisitFormDto;
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
@RequestMapping("/visits")
@Controller("ssrVisitCommandController")
public class VisitCommandController {
    private final VisitCreateUseCase createVisitUseCase;
    private final SsrVisitCreateFormUseCase visitCreateFormUseCase;

    @PostMapping("/new")
    public String createVisit(
            @RequestParam String petId,
            @RequestParam String ownerId,
            @Valid @ModelAttribute("form") VisitFormDto form,
            BindingResult binding,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (binding.hasErrors()) {
            model.addAttribute("visitForm", visitCreateFormUseCase.getVisitCreateFormByPetId(petId));
            model.addAttribute("form", form);
            model.addAttribute("error", errorMessage(binding));
            return "owner/form/visit-create-form";
        }
        request.setAttribute("visitForm", form);
        request.setAttribute("petId", petId);
        createVisitUseCase.createVisit(
                new VisitCreateCommand(
                        form.getDate(), form.getDescription(), petId, ownerId
                )
        );
        redirectAttributes.addFlashAttribute("message", "Visit added successfully");
        return "redirect:/owners/" + ownerId;
    }

    private static String errorMessage(BindingResult binding) {
        return binding.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}