package br.com.dalcatech.admin.catalogo.application.category.create;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.validation.handler.Notification;
import br.com.dalcatech.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase
    {
    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway)
        {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        }

    @Override
    public Either<Notification,CreateCategoryOutput> execute(final CreateCategoryCommand aCommand)
        {
        final var notification = Notification.create();
        final var aCategory = Category.newCategory(aCommand.name(),aCommand.description(),aCommand.isActive());
        aCategory.validate(notification);

        return notification.hasErrors() ? Left(notification) : create(aCategory);
        }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory)
        {
        return API.Try(() -> this.categoryGateway.create(aCategory))
                .toEither()
                .bimap(Notification::create,CreateCategoryOutput::from);
        }

    /* eh a mesma coisa de cima, so que encima uso o try do vavr
    private Either<Notification, CreateCategoryOutput> create1(final Category aCategory)
        {
        try
            {
            return API.Right(CreateCategoryOutput.from(this.categoryGateway.create(aCategory)));
            }
        catch (Throwable t)
            {
            return API.Left(Notification.create(t));
            }
        }
     */
    }
