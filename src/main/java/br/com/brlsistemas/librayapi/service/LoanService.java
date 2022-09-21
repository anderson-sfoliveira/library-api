package br.com.brlsistemas.librayapi.service;

import br.com.brlsistemas.librayapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);
}
