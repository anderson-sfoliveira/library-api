package br.com.brlsistemas.librayapi.api.service.impl;

import br.com.brlsistemas.librayapi.api.entity.Loan;
import br.com.brlsistemas.librayapi.api.repository.RepositoryLoan;
import br.com.brlsistemas.librayapi.api.service.LoanService;

public class LoanServiceImpl implements LoanService {
    private RepositoryLoan repositoryLoan;

    public LoanServiceImpl(RepositoryLoan repositoryLoan) {
        this.repositoryLoan = repositoryLoan;
    }

    @Override
    public Loan save(Loan loan) {
        return repositoryLoan.save(loan);
    }
}
