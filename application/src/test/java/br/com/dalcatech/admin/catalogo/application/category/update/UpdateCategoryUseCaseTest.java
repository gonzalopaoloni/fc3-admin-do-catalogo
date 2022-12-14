package br.com.dalcatech.admin.catalogo.application.category.update;

import br.com.dalcatech.admin.catalogo.application.category.create.CreateCategoryCommand;
import br.com.dalcatech.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
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
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest
    {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
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
    //3. Teste atualizando uma categoria inativa
    //4. Teste simulando um erro generico vindo do gateway
    //5. Teste atualizar category passando id invalido

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCaregoryId()
        {
        final var aCategory = Category.newCategory("Film","",true);

        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;


        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway , Mockito.times(1))
                .findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1 ))
                .update(Mockito.argThat(aUpdatedCategory ->
                                         Objects.equals(expectedName, aUpdatedCategory.getName())
                                            && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                            && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                            && Objects.equals(expectedId, aUpdatedCategory.getId())
                                            && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                            && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                            && Objects.isNull(aUpdatedCategory.getDeletedAt())));
        }


    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainExecption()
        {
        final var aCategory = Category.newCategory("Film","",true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

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

        final var expectedName = "Filme";
        final var expectedDescription = "A categoria mais vendida";
        final var expectedIsActive = false;


        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway , Mockito.times(1))
                .findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1 ))
                .update(Mockito.argThat(aUpdatedCategory ->
                                                Objects.equals(expectedName, aUpdatedCategory.getName())
                                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())));
        }


    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException()
        {
        final var aCategory = Category.newCategory("Film","",true);

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

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        Mockito.when(categoryGateway.update(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount,notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(1 ))
                .update(Mockito.argThat(aUpdatedCategory ->
                                                Objects.equals(expectedName, aUpdatedCategory.getName())
                                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),aUpdatedCategory.getCreatedAt())
                                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
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

        Mockito.when(categoryGateway.findById(CategoryID.from(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class,() -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorCount , actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage , actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway , Mockito.times(1))
                .findById(Mockito.eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway, Mockito.times(0 ))
                .update(Mockito.any());
        }
    }
