package br.com.dalcatech.admin.catalogo.application.category.create;

import br.com.dalcatech.admin.catalogo.IntegrationTest;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@IntegrationTest
public class CreateCategoryUseCaseIT
    {
    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCaregoryId()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0,categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1,categoryRepository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedName , actualCategory.getName());
        Assertions.assertEquals(expectedDescription , actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive , actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
        }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainExecption()
        {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0,categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Assertions.assertEquals(0,categoryRepository.count());

        //eh para ter certeza que nunca foi chamado indiferente do parametro
        Mockito.verify(categoryGateway , Mockito.times(0)).create(Mockito.any());
        }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCaregoryId()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = false;

        Assertions.assertEquals(0,categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1,categoryRepository.count());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedName , actualCategory.getName());
        Assertions.assertEquals(expectedDescription , actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive , actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        }


    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                        .when(categoryGateway).create(Mockito.any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());
        }
    }
