package br.com.dalcatech.admin.catalogo.application.category.retrieve.list;

import br.com.dalcatech.admin.catalogo.domain.category.Category;
import br.com.dalcatech.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        //Instant updatedAt,nao precisa para listagem
        Instant deletedAt)
    {

    //factory method
    public static CategoryListOutput from(final Category aCategory)
        {
        return new CategoryListOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getDeletedAt());
        }

    }
