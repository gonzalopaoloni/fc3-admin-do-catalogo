package br.com.dalcatech.admin.catalogo.infrastructure.category;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.dalcatech.admin.catalogo.domain.pagination.Pagination;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //poderia ser @Component
public class CategoryMySQLGateway implements CategoryGateway
    {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository)
        {
        this.repository = repository;
        }

    @Override
    public Category create(final Category aCategory)
        {
        return repository.save(CategoryJpaEntity.from(aCategory)).toAggredate();
        }

    @Override
    public void deleteById(CategoryID anId)
        {

        }

    @Override
    public Optional<Category> findById(CategoryID anId)
        {
        return Optional.empty();
        }

    @Override
    public Category update(Category aCategory)
        {
        return null;
        }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery)
        {
        return null;
        }
    }
