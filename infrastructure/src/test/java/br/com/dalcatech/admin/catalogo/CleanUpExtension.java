package br.com.dalcatech.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class CleanUpExtension implements BeforeEachCallback
    {
    //faco esta classe para a cada teste feito limpar as tabelas criadas no anterior
    @Override
    public void beforeEach(ExtensionContext context) throws Exception
        {
        final var repositories = SpringExtension.getApplicationContext(context).getBeansOfType(CrudRepository.class).values();
        cleanUp(repositories);
        }

    private void cleanUp(final Collection<CrudRepository> repositories)
        {
        repositories.forEach(CrudRepository::deleteAll);
        }
    }
