package dev.dmitriirussu.petclinic.application.query.repository;

import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;

public interface FindOwnerListRepository {
    PageResult<OwnerListView> findOwnerList(OwnerSearchCriteria criteria, PageQuery query);
}