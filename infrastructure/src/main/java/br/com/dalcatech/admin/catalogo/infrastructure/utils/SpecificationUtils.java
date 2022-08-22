package br.com.dalcatech.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils
    {
    //coloco final para ninguem possa extender dessa classe

    //crio um construtor privado para que se alguem force nao consiga instanciar dessa classe
    private SpecificationUtils() {};

    public static <T>Specification<T> like(final String prop, final String term)
        {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get(prop)), like(term.toUpperCase()));
        }

    private static String like(final String term)
        {
        return "%" + term.toUpperCase() + "%";
        }
    }
