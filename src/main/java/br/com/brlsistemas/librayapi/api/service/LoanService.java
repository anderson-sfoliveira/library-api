package br.com.brlsistemas.librayapi.api.service;

import br.com.brlsistemas.librayapi.api.entity.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    void update(Loan loan);
}
