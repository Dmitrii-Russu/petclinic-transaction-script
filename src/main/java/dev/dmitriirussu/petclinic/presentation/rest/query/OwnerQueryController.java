package dev.dmitriirussu.petclinic.presentation.rest.query;

import dev.dmitriirussu.petclinic.application.query.usecase.OwnerFindUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.OwnerSearchUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.application.query.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/owners")
@RestController("restOwnerQueryController")
public class OwnerQueryController {
    private static final int PAGE_SIZE = 5;

    private final OwnerFindUseCase ownerFindUseCase;
    private final OwnerSearchUseCase ownerSearchUseCase;

    @GetMapping
    public PageResult<OwnerListView> search(
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "1") @Min(1) int page
    ) {
        return ownerSearchUseCase.search(
                new OwnerSearchCriteria(lastName),
                new PageQuery(page, PAGE_SIZE)
        );
    }

    @GetMapping("/{id}")
    public OwnerDetailsView findById(@PathVariable String id) {
        return ownerFindUseCase.findById(id);
    }
}