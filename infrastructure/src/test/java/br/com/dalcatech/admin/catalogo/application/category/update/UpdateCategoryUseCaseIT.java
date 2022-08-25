package br.com.dalcatech.admin.catalogo.application.category.update;

import br.com.dalcatech.admin.catalogo.IntegrationTest;
import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.exceptions.DomainException;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@IntegrationTest
public class UpdateCategoryUseCaseIT
    {
    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    private void save(final Category... aCategory)
        {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList());
        }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCaregoryId()
        {
        final var aCategory = Category.newCategory("Film", "", true);

        save(aCategory);

        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Assertions.assertEquals(1,categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1,categoryRepository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName , actualCategory.getName());
        Assertions.assertEquals(expectedDescription , actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive , actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
        }


    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainExecption()
        {
        final var aCategory = Category.newCategory("Film","",true);

        save(aCategory);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(1,categoryRepository.count());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        //eh para ter certeza que nunca foi chamado indiferente do parametro
        Mockito.verify(categoryGateway , Mockito.times(0)).update(Mockito.any());
        }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactivateCaregoryId()
        {
        final var aCategory = Category.newCategory("Film","",true);

        save(aCategory);

        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = false;


        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);


        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName , actualCategory.getName());
        Assertions.assertEquals(expectedDescription , actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive , actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        }


    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException()
        {
        final var aCategory = Category.newCategory("Film","",true);

        save(aCategory);

        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                        .when(categoryGateway).update(Mockito.any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName() , actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription() , actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive() , actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
        }


    @Test
    public void givenAValidCommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive);


        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount , actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage , actualException.getErrors().get(0).message());

        }
    }
