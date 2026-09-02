package dev.dmitriirussu.petclinic.presentation.rest.query;

import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerListUseCase;
import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
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

    private final FindOwnerUseCase findOwnerUseCase;
    private final FindOwnerListUseCase findOwnerListUseCase;

    @GetMapping
    public PageResult<OwnerListView> findAll(
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "1") @Min(1) int page
    ) {
        return findOwnerListUseCase.findOwnerList(
                new OwnerSearchCriteria(lastName),
                new PageQuery(page, PAGE_SIZE)
        );
    }

    @GetMapping("/{ownerId}")
    public OwnerDetailsView findById(@PathVariable String ownerId) {
        return findOwnerUseCase.findByOwnerId(ownerId);
    }
}