package br.com.dalcatech.admin.catalogo.application.category.retrieve.list;

import br.com.dalcatech.admin.catalogo.IntegrationTest;
import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
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
                        Category.newCategory("NetFlix originals", "Titulos da autoria do netflix", true),
                        Category.newCategory("Documentarios", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", null, true)
                                        )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
        }

    public void giverAValidTerm_whenTermIsNot

    }
