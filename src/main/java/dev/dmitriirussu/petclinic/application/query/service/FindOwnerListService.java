package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.FindOwnerListRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.FindOwnerListUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class FindOwnerListService implements FindOwnerListUseCase {
    private final FindOwnerListRepository repository;

    public PageResult<OwnerListView> findOwnerList(OwnerSearchCriteria criteria, PageQuery query) {
        return repository.findOwnerList(criteria, query);
    }
}
