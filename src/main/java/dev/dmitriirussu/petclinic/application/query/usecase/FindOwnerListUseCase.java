package dev.dmitriirussu.petclinic.application.query.usecase;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;

public interface FindOwnerListUseCase {
    PageResult<OwnerListView> findOwnerList(OwnerSearchCriteria criteria, PageQuery query);
}
