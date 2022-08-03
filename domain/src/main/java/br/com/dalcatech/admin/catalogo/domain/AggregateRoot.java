package br.com.dalcatech.admin.catalogo.domain;

import br.com.dalcatech.admin.catalogo.domain.validation.ValidationHandler;

public class AggregateRoot<ID extends Identifier> extends Entity<ID>
    {
    //PADRAO DO DDD
    protected AggregateRoot(final ID id)
        {
        super(id);
        }

    @Override
    public void validate(ValidationHandler handler)
        {

        }
    }
