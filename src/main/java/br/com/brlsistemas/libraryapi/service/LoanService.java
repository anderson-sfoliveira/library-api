package br.com.brlsistemas.libraryapi.service;

import br.com.brlsistemas.libraryapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    List<Loan> getAllLateLoans();
}
