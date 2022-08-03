package br.com.dalcatech.admin.catalogo.application;

public abstract class UnitUseCase<IN>
    {
    //recebe alguma coisa, e retorna nada
    public abstract void execute(IN anIn);
    }
