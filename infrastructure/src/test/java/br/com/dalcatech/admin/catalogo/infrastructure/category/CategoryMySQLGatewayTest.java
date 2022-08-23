package br.com.dalcatech.admin.catalogo.infrastructure.category;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.dalcatech.admin.catalogo.MySQLGatewayTest;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest
    {
    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void testInjectedDependencies()
        {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(categoryRepository);
        }

    @Test
    public void givenAValidCategory_whenCallsCreate_ShouldReturnANewCategory()
        {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName,expectedDescription,expectedIsActive);

        Assertions.assertEquals(0,categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);

        Assertions.assertEquals(1,categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
        Assertions.assertEquals(expectedName,actualEntity.getName());
        Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive,actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
        }


    @Test
    public void givenAValidCategory_whenCallsUpdate_ShouldReturnCategoryUpdated()
        {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film",null,expectedIsActive);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals("Film",actualInvalidEntity.getName());
        Assertions.assertNull(actualInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive,actualInvalidEntity.isActive());

        final var aUpdatedCategory =
                aCategory.clone().update(expectedName,expectedDescription,expectedIsActive);

        final var actualCategory = categoryGateway.update(aUpdatedCategory);

        Assertions.assertEquals(1,categoryRepository.count());
        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
        Assertions.assertEquals(expectedName,actualEntity.getName());
        Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive,actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
        }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory()
        {
        final var aCategory = Category.newCategory("Filmes",null,true);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0,categoryRepository.count());
        }

    @Test
    public void givenAInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory()
        {
        Assertions.assertEquals(0,categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("123"));

        Assertions.assertEquals(0,categoryRepository.count());
        }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_ShouldReturnCategory()
        {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName,expectedDescription,expectedIsActive);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1,categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
        }

    @Test
    public void givenInvalidCategoryIdNotStored_whenCallsFindById_ShouldReturnEmpty()
        {
        Assertions.assertEquals(0,categoryRepository.count());

        final var actualCategory = categoryGateway.findById(CategoryID.from("empty"));

        Assertions.assertTrue(actualCategory.isEmpty());
        }

    @Test
    public void givenAPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated()
        {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes" , null, true);
        final var series = Category.newCategory("Series" , null, true);
        final var documentarios = Category.newCategory("Documentarios" , null, true);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        Assertions.assertEquals(expectedTotal,categoryRepository.count());

        final var query = new CategorySearchQuery(0,1,"","name","asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(),actualResult.items().get(0).getId());
        }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage()
        {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0,categoryRepository.count());

        final var query = new CategorySearchQuery(0,1,"","name","asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(0,actualResult.items().size());
        }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated()
        {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes" , null, true);
        final var series = Category.newCategory("Series" , null, true);
        final var documentarios = Category.newCategory("Documentarios" , null, true);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        Assertions.assertEquals(expectedTotal,categoryRepository.count());

        var query = new CategorySearchQuery(0,1,"","name","asc");
        var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(),actualResult.items().get(0).getId());

        //Page 1
        expectedPage = 1;
        query = new CategorySearchQuery(1,1,"","name","asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(filmes.getId(),actualResult.items().get(0).getId());

        //Page 2
        expectedPage = 2;
        query = new CategorySearchQuery(2,1,"","name","asc");
        actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(series.getId(),actualResult.items().get(0).getId());
        }

    @Test
    public void givenAPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated()
        {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes" , null, true);
        final var series = Category.newCategory("Series" , null, true);
        final var documentarios = Category.newCategory("Documentarios" , null, true);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        Assertions.assertEquals(3,categoryRepository.count());

        final var query = new CategorySearchQuery(0,1,"doc","name","asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.items().size());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(),actualResult.items().get(0).getId());
        }

    @Test
    public void givenAPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescrition_shouldReturnPaginated()
        {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes" , "A categoria mais assistida", true);
        final var series = Category.newCategory("Series" , "Uma categoria assitida", true);
        final var documentarios = Category.newCategory("Documentarios" , "A cateogria menos assistida", true);

        Assertions.assertEquals(0,categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        Assertions.assertEquals(3,categoryRepository.count());

        final var query = new CategorySearchQuery(0,1,"mais assistida","name","asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage,actualResult.items().size());
        Assertions.assertEquals(filmes.getId(),actualResult.items().get(0).getId());
        }

    }
