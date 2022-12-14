package br.com.dalcatech.admin.catalogo.infrastructure;

import br.com.dalcatech.admin.catalogo.application.category.create.CreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.dalcatech.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import br.com.dalcatech.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import br.com.dalcatech.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main
    {
    public static void main(String[] args)
        {
        System.out.println("Hello world!");
        //System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME,"development");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
        }

    /*
    @Bean
    public ApplicationRunner runner(CategoryRepository repository)
        {
        return args ->
            {
            List<CategoryJpaEntity> all = repository.findAll();
            Category filmes = Category.newCategory("Filmes", null, true);
            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
            repository.deleteAll();
            };
        }


    @Bean
    @DependsOnDatabaseInitialization
    public ApplicationRunner runner(
            @Autowired CreateCategoryUseCase createCategoryUseCase,
            @Autowired UpdateCategoryUseCase updateCategoryUseCase,
            @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase,
            @Autowired ListCategoriesUseCase listCategoriesUseCase,
            @Autowired DeleteCategoryUseCase deleteCategoryUseCase
            )
        {
        return args ->
            {

            };
        }
     */
    }