package dev.dmitriirussu.petclinic.presentation.ssr.query;

import dev.dmitriirussu.petclinic.application.query.usecase.SsrVisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.VisitFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visits")
public class SsrVisitQueryController {
    private final SsrVisitCreateFormUseCase useCase;

    @GetMapping("/new")
    public String showNewVisitForm(Model model, @RequestParam String petId) {
        SsrVisitCreateView visitCreateView = useCase.getVisitCreateFormByPetId(petId);
        VisitFormDto form = new VisitFormDto();
        model.addAttribute("visitForm", visitCreateView);
        model.addAttribute("form", form);
        model.addAttribute("error", "");
        return "owner/form/visit-create-form";
    }
}