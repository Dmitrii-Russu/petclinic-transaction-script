package dev.dmitriirussu.petclinic.presentation.ssr.query;

import dev.dmitriirussu.petclinic.application.query.usecase.ssr.VisitCreateFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.visit.ssr.VisitCreateView;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.VisitFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/visits")
@Controller("ssrVisitQueryController")
public class VisitQueryController {
    private final VisitCreateFormUseCase visitFormUseCase;

    @GetMapping("/new")
    public String showNewVisitForm(Model model, @RequestParam String petId) {
        VisitCreateView visitCreateView = visitFormUseCase.findVisitCreateForm(petId);
        VisitFormDto form = new VisitFormDto();
        model.addAttribute("visitForm", visitCreateView);
        model.addAttribute("form", form);
        model.addAttribute("error", "");
        return "owner/form/visit-create-form";
    }
}