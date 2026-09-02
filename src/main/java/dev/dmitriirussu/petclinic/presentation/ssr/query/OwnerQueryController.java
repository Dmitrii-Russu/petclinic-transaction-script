package dev.dmitriirussu.petclinic.presentation.ssr.query;

import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerListUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrOwnerEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerEditView;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.OwnerFormDto;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/owners")
@Controller("ssrOwnerQueryController")
public class OwnerQueryController {
    private static final int PAGE_SIZE = 5;

    private final FindOwnerUseCase findOwnerUseCase;
    private final FindOwnerListUseCase findOwnerListUseCase;
    private final SsrOwnerEditFormUseCase ownerEditFormUseCase;

    // --- Search ---
    @GetMapping
    public String showOwnerSearch(Model model) {
        model.addAttribute("error", "");
        model.addAttribute("message", "");
        return "owner/form/main-page-search";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            Model model
    ) {
        var criteria = new OwnerSearchCriteria(lastName);
        var query = new PageQuery(page, PAGE_SIZE);
        PageResult<OwnerListView> result = findOwnerListUseCase.findOwnerList(criteria, query);

        if (page > result.totalPages() && result.total() > 0) {
            model.addAttribute("error", "Invalid page number");
            model.addAttribute("message", "");
            return "owner/form/main-page-search";
        }

        if (result.isEmpty()) {
            model.addAttribute("error", "No owners found");
            model.addAttribute("message", "");
            return "owner/form/main-page-search";
        }

        if (result.total() == 1) {
            return "redirect:/owners/" + result.content().get(0).id();
        }

        model.addAttribute("owners", result.content());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", result.totalPages());
        model.addAttribute("lastName", lastName != null ? lastName : "");
        return "owner/result/owners-list";
    }

    // --- Details ---
    @GetMapping("/{ownerId}")
    public String showOwnerDetails(
            @PathVariable String ownerId,
            @ModelAttribute("message") String message,
            Model model
    ) {
        OwnerDetailsView owner = findOwnerUseCase.findById(ownerId);
        model.addAttribute("owner", owner);
        model.addAttribute("message", message);
        return "owner/result/owner-details";
    }

    // --- Forms ---
    @GetMapping("/new")
    public String showNewOwnerForm(Model model) {
        model.addAttribute("ownerForm", new OwnerFormDto());
        model.addAttribute("ownerId", null);
        model.addAttribute("error", "");
        return "owner/form/owner-create-edit-form";
    }

    @GetMapping("/{ownerId}/edit")
    public String showEditOwnerForm(@PathVariable String ownerId, Model model) {
        SsrOwnerEditView view = ownerEditFormUseCase.getOwnerEditFormByOwnerId(ownerId);
        model.addAttribute("ownerForm", OwnerFormDto.from(view));
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("error", "");
        return "owner/form/owner-create-edit-form";
    }
}