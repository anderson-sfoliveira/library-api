package br.com.brlsistemas.librayapi.api.repository;

import br.com.brlsistemas.librayapi.api.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryLoan extends JpaRepository<Loan, Long> {
}
