package br.com.dalcatech.admin.catalogo.application.category.create;

public record CreateCategoryCommand(String name,String description,boolean isActive)
    {
    public static CreateCategoryCommand with(final String aName,final String aDescription,final boolean isActive)
        {
        //padrao factorymethod
        return new CreateCategoryCommand(aName,aDescription,isActive);
        }
    }
