package br.com.dalcatech.admin.catalogo.domain.exceptions;

import br.com.dalcatech.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException
    {
    private final List<Error> errors;

    private DomainException(final String aMessage, final List<Error> anErrors)
        {
        super(aMessage);  //construtor padrao da runtimeexception
        this.errors = anErrors;
        }

    //FactoryMethod padrao de projeto
    public static DomainException with(final Error anError)
        {
        return new DomainException(anError.message(), List.of(anError));
        }
    public static DomainException with(final List<Error> anErrors)
        {
        return new DomainException("",anErrors);
        }

    public List<Error> getErrors()
        {
        return errors;
        }
    }
