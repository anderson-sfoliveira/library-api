package br.com.brlsistemas.librayapi.api.service.impl;

import br.com.brlsistemas.librayapi.api.entity.Loan;
import br.com.brlsistemas.librayapi.api.repository.LoanRepository;
import br.com.brlsistemas.librayapi.api.service.LoanService;
import br.com.brlsistemas.librayapi.exception.BusinessException;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {
    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if ( loanRepository.existsByBookAndNotReturned(loan.getBook()) ) {
            throw new BusinessException("Book already loaned");
        }
        return loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return loanRepository.save(loan);
    }
}
