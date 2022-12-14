package br.com.dalcatech.admin.catalogo.infrastructure.api.controllers;

import br.com.dalcatech.admin.catalogo.application.category.create.CreateCategoryUseCase;
import br.com.dalcatech.admin.catalogo.domain.pagination.Pagination;
import br.com.dalcatech.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CategoryController implements CategoryAPI
    {
    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase)
        {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        }

    @Override
    public ResponseEntity<?> createCategory()
        {
        return null;
        }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction)
        {
        return null;
        }
    }
