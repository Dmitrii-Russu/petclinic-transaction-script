package dev.dmitriirussu.petclinic.application.query.service;

import dev.dmitriirussu.petclinic.application.query.repository.OwnerSearchRepository;
import dev.dmitriirussu.petclinic.application.query.usecase.OwnerSearchUseCase;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.application.query.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class OwnerSearchService implements OwnerSearchUseCase {
    private final OwnerSearchRepository repository;

    public PageResult<OwnerListView> search(OwnerSearchCriteria criteria, PageQuery query) {
        return repository.search(criteria, query);
    }
}
