package br.com.dalcatech.admin.catalogo.domain.category;


import br.com.dalcatech.admin.catalogo.domain.AggregateRoot;
import br.com.dalcatech.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable
    {
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public CategoryID getId()
        {
        return id;
        }

    public String getName()
        {
        return name;
        }

    public String getDescription()
        {
        return description;
        }

    public boolean isActive()
        {
        return isActive;
        }

    public Instant getCreatedAt()
        {
        return createdAt;
        }

    public Instant getUpdatedAt()
        {
        return updatedAt;
        }

    public Instant getDeletedAt()
        {
        return deletedAt;
        }

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeletedDate)
        {
        super(anId);
        this.name = aName; //o nome e verificado no validator, porque tem que mostrar ao usuario o erro
        this.description = aDescription;
        this.isActive = isActive;
        //createdAt e updatedAt nulos seriam bug de desenvolvedor, entao a gente separa com a parte do formulario, do usuario final
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null'");
        this.updatedAt = Objects.requireNonNull(aUpdateDate,"'updatedAt' should not be null'");
        this.deletedAt = aDeletedDate;
        }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive)
        {
        final var id = CategoryID.unique();
        var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
        }

    public static Category with(final Category aCategory)
        {
        return with(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive,
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
        }

    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt)
        {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt);
        }

    @Override
    public void validate(final ValidationHandler handler)
        {
        new CategoryValidator(this, handler).validate();
        }

    public Category deactivate()
        {
        if (getDeletedAt() == null)
            {
            this.deletedAt = Instant.now();
            }

        this.isActive = false;
        this.updatedAt = Instant.now();
        return this;
        }

    public Category activate()
        {
        this.deletedAt = null;
        this.isActive = true;
        this.updatedAt = Instant.now();
        return this;
        }

    public Category update(final String aName, final String aDescription, final boolean isActive)
        {
        if (isActive)
            activate();
        else
            deactivate();
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;
        }


    @Override
    public Category clone()
        {
        try
            {
            return (Category) super.clone();
            }
        catch (CloneNotSupportedException e)
            {
            throw new AssertionError();
            }
        }
    }
