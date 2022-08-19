package br.com.dalcatech.admin.catalogo.infrastructure;

import br.com.dalcatech.admin.catalogo.infrastructure.category.CategoryMySQLGatewayTest;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited  //caso alguem precisar extender a anotacao
@ActiveProfiles("test")
@DataJpaTest
//poderia usar @SpringBootTest mas eh demorado demais, mas como uso o DataJpaTest, preciso inserir o componetScan para adicionar as classes MySQLGateway
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySQLGateway]")
})
@ExtendWith(MySQLGatewayTest.CleanUpExtensions.class)
public @interface MySQLGatewayTest
    {
    class CleanUpExtensions implements BeforeEachCallback
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
    }
