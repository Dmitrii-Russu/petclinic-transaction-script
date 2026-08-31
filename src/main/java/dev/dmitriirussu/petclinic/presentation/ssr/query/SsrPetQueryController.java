package dev.dmitriirussu.petclinic.presentation.ssr.query;

import dev.dmitriirussu.petclinic.application.query.catalog.PetTypeCatalog;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrOwnerNameUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.SsrPetEditFormUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.SsrOwnerNameView;
import dev.dmitriirussu.petclinic.application.query.view.pet.SsrPetEditView;
import dev.dmitriirussu.petclinic.presentation.ssr.dto.PetFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pets")
public class SsrPetQueryController {
    private final SsrPetEditFormUseCase petEditFormUseCase;
    private final SsrOwnerNameUseCase ownerNameUseCase;
    private final PetTypeCatalog catalog;

    @GetMapping("/new")
    public String showNewPetForm(Model model, @RequestParam String ownerId) {
        SsrOwnerNameView owner = ownerNameUseCase.getOwnerNameById(ownerId);
        List<String> petTypes = catalog.getAllTypes();
        PetFormDto form = new PetFormDto();
        form.setOwnerId(ownerId);
        form.setOwnerFirstName(owner.firstName());
        form.setOwnerLastName(owner.lastName());
        model.addAttribute("ownerFirstName", owner.firstName());
        model.addAttribute("ownerLastName", owner.lastName());
        model.addAttribute("petTypes", petTypes);
        model.addAttribute("petForm", form);
        model.addAttribute("petId", null);
        model.addAttribute("error", "");
        return "owner/form/pet-create-edit-form";
    }

    @GetMapping("/{petId}/edit")
    public String showEditPetForm(@PathVariable String petId, Model model) {
        SsrPetEditView pet = petEditFormUseCase.getPetEditFormById(petId);
        List<String> petTypes = catalog.getAllTypes();
        model.addAttribute("ownerFirstName", pet.firstName());
        model.addAttribute("ownerLastName", pet.lastName());
        model.addAttribute("petTypes", petTypes);
        model.addAttribute("petForm", PetFormDto.from(pet));
        model.addAttribute("petId", petId);
        model.addAttribute("error", "");
        return "owner/form/pet-create-edit-form";
    }
}