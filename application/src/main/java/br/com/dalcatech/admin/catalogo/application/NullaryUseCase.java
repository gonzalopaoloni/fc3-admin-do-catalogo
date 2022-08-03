package br.com.dalcatech.admin.catalogo.application;

public abstract class NullaryUseCase<OUT>
    {
    //recebe nada e retorna alguma coisa
    public abstract OUT execute();

    }
