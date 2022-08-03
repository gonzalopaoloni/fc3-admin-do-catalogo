package br.com.dalcatech.admin.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(int currentPage, int perPage, long total, List<T> items)
    {
    //record eh um DTO imutavel para passar dados de um lado para outro
    public <R> Pagination<R> map(final Function<T,R> mapper)
        {
        final List<R> aNewList = this.items.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(currentPage(),perPage(),total(),aNewList);
        }
    }
