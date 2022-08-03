package br.com.dalcatech.admin.catalogo.application.category.retrieve.get;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.exceptions.DomainException;
import br.com.dalcatech.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase
    {
    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway)
        {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        }


    @Override
    public CategoryOutput execute(final String anIn)
        {
        final var aCategoryID = CategoryID.from(anIn);

        return this.categoryGateway.findById(aCategoryID)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(aCategoryID));

        }

    private Supplier<DomainException> notFound(final CategoryID anId)
        {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
        }
    }
