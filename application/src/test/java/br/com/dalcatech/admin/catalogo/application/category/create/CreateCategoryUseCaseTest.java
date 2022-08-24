package br.com.dalcatech.admin.catalogo.application.category.create;

import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest
    {
    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;
    @BeforeEach
    void cleanUp()
        {
        //antes de fazer qualquer teste, eu reseto as classes que foram
        // mocadas assim nao eh passado nada para outros metoidos
        Mockito.reset(categoryGateway);
        }
    //1. Teste do caminho feliz
    //2. Teste passando uma propriedade invalida (name)
    //3. Teste criando uma categoria inativa
    //4. Teste simulando um erro generico vindo do gateway

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCaregoryId()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                                            {
                                            return Objects.equals(expectedName, aCategory.getName())
                                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                                    && Objects.nonNull(aCategory.getId())
                                                    && Objects.nonNull(aCategory.getCreatedAt())
                                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                                    && Objects.isNull(aCategory.getDeletedAt());
                                            }));
        }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainExecption()
        {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        //eh para ter certeza que nunca foi chamado indiferente do parametro
        Mockito.verify(categoryGateway , Mockito.times(0)).create(Mockito.any());
        }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCaregoryId()
        {
        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                                            {
                                            return Objects.equals(expectedName, aCategory.getName())
                                                   && Objects.equals(expectedDescription, aCategory.getDescription())
                                                   && Objects.equals(expectedIsActive, aCategory.isActive())
                                                   && Objects.nonNull(aCategory.getId())
                                                   && Objects.nonNull(aCategory.getCreatedAt())
                                                   && Objects.nonNull(aCategory.getUpdatedAt())
                                                   && Objects.nonNull(aCategory.getDeletedAt());
                                            }));
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

        Mockito.when(categoryGateway.create(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        //ele foi chamado uma vez o gateway no metodo create
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                                            {
                                            return Objects.equals(expectedName, aCategory.getName())
                                                   && Objects.equals(expectedDescription, aCategory.getDescription())
                                                   && Objects.equals(expectedIsActive, aCategory.isActive())
                                                   && Objects.nonNull(aCategory.getId())
                                                   && Objects.nonNull(aCategory.getCreatedAt())
                                                   && Objects.nonNull(aCategory.getUpdatedAt())
                                                   && Objects.isNull(aCategory.getDeletedAt());
                                            }));
        }
    }
