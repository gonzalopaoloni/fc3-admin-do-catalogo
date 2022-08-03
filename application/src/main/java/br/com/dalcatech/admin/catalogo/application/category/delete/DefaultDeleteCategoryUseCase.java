package br.com.dalcatech.admin.catalogo.application.category.delete;

import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase
    {
    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway)
        {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        }


    @Override
    public void execute(final String anIn)
        {
        this.categoryGateway.deleteById(CategoryID.from(anIn));
        }
    }
