package br.com.dalcatech.admin.catalogo.application.category.delete;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class DeleteCategoryUseCaseTest
    {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp()
        {
        Mockito.reset(categoryGateway);
        }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnOK()
        {
        final var aCategory = Category.newCategory("Filmes","A cateoria mais assistida",true);
        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(Mockito.eq(expectedId));
        }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldReturnOK()
        {
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing()
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(Mockito.eq(expectedId));
        }

    @Test
    public void givenAValidID_whenGatewayThrowsException_shouldReturnException()
        {
        final var aCategory = Category.newCategory("Filmes","A cateoria mais assistida",true);
        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(Mockito.eq(expectedId));
        }
    }
