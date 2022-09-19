package br.com.brlsistemas.librayapi.api.service.impl;

import br.com.brlsistemas.librayapi.api.entity.Loan;
import br.com.brlsistemas.librayapi.api.repository.RepositoryLoan;
import br.com.brlsistemas.librayapi.api.service.LoanService;
import br.com.brlsistemas.librayapi.exception.BusinessException;

public class LoanServiceImpl implements LoanService {
    private RepositoryLoan repositoryLoan;

    public LoanServiceImpl(RepositoryLoan repositoryLoan) {
        this.repositoryLoan = repositoryLoan;
    }

    @Override
    public Loan save(Loan loan) {
        if ( repositoryLoan.existsByBookAndNotReturned(loan.getBook()) ) {
            throw new BusinessException("Book already loaned");
        }
        return repositoryLoan.save(loan);
    }
}
