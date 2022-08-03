package br.com.dalcatech.admin.catalogo.application.category.create;

import br.com.dalcatech.admin.catalogo.application.UseCase;
import br.com.dalcatech.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification,CreateCategoryOutput>>
    {
    //eh a abstracao do createcategoryusecase, nao trabalhe para uma implementacao, trabalhe para uma abstracao
    }
