package br.com.dalcatech.admin.catalogo.infrastructure.category;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryGateway;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;
import br.com.dalcatech.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.dalcatech.admin.catalogo.domain.pagination.Pagination;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.dalcatech.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import br.com.dalcatech.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    private Category save(final Category aCategory)
        {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggredate();
        }
    @Override
    public Category create(final Category aCategory)
        {
        return save(aCategory);
        }

    @Override
    public void deleteById(CategoryID anId)
        {
        String anIdValue = anId.getValue();
        if(this.repository.existsById(anIdValue))
            {
            this.repository.deleteById(anIdValue);
            }
        }

    @Override
    public Optional<Category> findById(CategoryID anId)
        {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggredate);
        }

    @Override
    public Category update(final Category aCategory)
        {
        return save(aCategory);
        }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery)
        {
        //Paginacao
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort()));

        //Busca dinamica pelo criterio terms (name ou description)
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name" , str);
                    final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description",str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);

        final var pageResult =this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggredate).toList());
        }
    }
