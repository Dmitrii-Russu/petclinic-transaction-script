package dev.dmitriirussu.petclinic.infrastructure.query;

import dev.dmitriirussu.petclinic.application.query.repository.FindOwnerListRepository;
import dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView;
import dev.dmitriirussu.petclinic.shared.SqlLoader;
import dev.dmitriirussu.petclinic.infrastructure.query.support.ViewExtractor;
import dev.dmitriirussu.petclinic.shared.pagination.OwnerSearchCriteria;
import dev.dmitriirussu.petclinic.shared.pagination.PageQuery;
import dev.dmitriirussu.petclinic.shared.pagination.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@RequiredArgsConstructor
class FindOwnerListRepositoryImpl implements FindOwnerListRepository {
    private final JdbcClient jdbc;
    private static final String OWNER_LIST_COUNT_SQL =
            SqlLoader.load("sql/query/find-owner-list-count.sql");
    private static final String OWNER_LIST_SQL =
            SqlLoader.load("sql/query/find-owner-list.sql");

    @Override
    public PageResult<OwnerListView> findOwnerList(OwnerSearchCriteria criteria, PageQuery query) {
        String prefix = criteria.lastNamePrefix();

        long total = jdbc.sql(OWNER_LIST_COUNT_SQL)
                .param("lastNamePrefix", prefix)
                .query(Long.class)
                .single();

        List<OwnerListView> content = jdbc.sql(OWNER_LIST_SQL)
                .param("lastNamePrefix", prefix)
                .param("size",   query.size())
                .param("offset", (long) (query.page() - 1) * query.size())
                .query(ViewExtractor::getOwnerWithPetNames)
                .list();

        return new PageResult<>(content, query.page(), query.size(), total);
    }
}
