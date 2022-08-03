package br.com.dalcatech.admin.catalogo.application.category.retrieve.list;

import br.com.dalcatech.admin.catalogo.application.UseCase;
import br.com.dalcatech.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.dalcatech.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>>
    {
    }
