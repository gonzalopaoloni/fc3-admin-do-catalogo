package br.com.dalcatech.admin.catalogo.application;

public abstract class UseCase<IN,OUT>
    {
    //os caso de usos implementam o pattern command
    //recebe alguma coisa e retorna outra coisa
    public abstract OUT execute(IN anIn);
    }