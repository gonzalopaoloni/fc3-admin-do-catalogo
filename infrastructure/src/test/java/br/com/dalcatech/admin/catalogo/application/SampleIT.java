package br.com.dalcatech.admin.catalogo.application;

import br.com.dalcatech.admin.catalogo.IntegrationTest;
import br.com.dalcatech.admin.catalogo.application.category.create.CreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT
    {
    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void test()
        {
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(categoryRepository);
        }
    }
