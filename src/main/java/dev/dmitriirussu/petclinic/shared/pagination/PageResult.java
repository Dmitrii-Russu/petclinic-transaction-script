package dev.dmitriirussu.petclinic.shared.pagination;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(List<T> content, int page, int size, long total) {
    public int totalPages() {
        return (int) Math.ceil((double) total / size);
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public <R> PageResult<R> map(Function<T, R> mapper) {
        return new PageResult<>(
                content.stream().map(mapper).toList(),
                page,
                size,
                total
        );
    }
}
