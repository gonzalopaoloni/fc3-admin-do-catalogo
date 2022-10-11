package br.com.dalcatech.admin.catalogo.application.category.retrieve.list;

import br.com.dalcatech.admin.catalogo.IntegrationTest;
import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoryUseCaseIT
    {
    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    private void mockUp()
        {
        final var categories = Stream.of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("Series", null, true),
                        Category.newCategory("Amazon originals", "Titulos da autoria da AmazonPrime", true),
                        Category.newCategory("Netflix originals", "Titulos da autoria do netflix", true),
                        Category.newCategory("Documentarios", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "Categoria para criancas", true)
                                        )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
        }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_ShouldReturnEmptyPage()
        {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "123ds a dasd 12 1das";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount , actualResult.items().size());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix originals",
            "ZON,0,10,1,1,Amazon originals",
            "KI,0,10,1,1,Kids",
            "criancas,0,10,1,1,Kids",
            "da amazon,0,10,1,1,Amazon originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(final String expectedTerms,final int expectedPage,final int expectedPerPage,final int expectedItemsCount,final long expectedTotal, final String expectedCategoryName)
        {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount , actualResult.items().size());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedCategoryName,actualResult.items().get(0).name());
        }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Kids",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(final String expectedSort,final String expectedDirection,final int expectedPage,final int expectedPerPage,final int expectedItemsCount,final long expectedTotal, final String expectedCategoryName)
        {
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount , actualResult.items().size());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedCategoryName,actualResult.items().get(0).name());
        }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Filmes;Series",
            "1,2,2,7,Amazon originals;Netflix originals",
            "2,2,2,7,Documentarios;Sports",
            "3,2,1,7,Kids",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(final int expectedPage,final int expectedPerPage,final int expectedItemsCount,final long expectedTotal, final String expectedCategoriesName)
        {
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";


        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount , actualResult.items().size());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());

        int index = 0 ;
        for (final String expectedName : expectedCategoriesName.split(";"))
            {
            Assertions.assertEquals(expectedName,actualResult.items().get(index).name());
            index++;
            }


        }
    }
