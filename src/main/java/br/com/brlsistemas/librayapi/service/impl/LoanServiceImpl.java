package br.com.brlsistemas.librayapi.service.impl;

import br.com.brlsistemas.librayapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.model.repository.LoanRepository;
import br.com.brlsistemas.librayapi.service.LoanService;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
        return loanRepository.findByBookIsbnOrCustomer(loanFilterDTO.getIsbn(), loanFilterDTO.getCustomer(), pageable);
    }
}
