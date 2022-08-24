package br.com.dalcatech.admin.catalogo.infrastructure.configuration.usescases;

import br.com.dalcatech.admin.catalogo.application.category.create.CreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import br.com.dalcatech.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig
    {
    //injecao de dependencias dos casos de usos
    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway)
        {
        this.categoryGateway = categoryGateway;
        }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase()
        {
        return new DefaultCreateCategoryUseCase(categoryGateway);
        }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase()
        {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
        }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase()
        {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
        }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase()
        {
        return new DefaultListCategoriesUseCase(categoryGateway);
        }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase()
        {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
        }
    }
