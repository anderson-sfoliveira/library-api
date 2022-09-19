package br.com.brlsistemas.librayapi.api.service;

import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.entity.Loan;
import br.com.brlsistemas.librayapi.api.repository.RepositoryLoan;
import br.com.brlsistemas.librayapi.api.service.impl.BookServiceImpl;
import br.com.brlsistemas.librayapi.api.service.impl.LoanServiceImpl;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    private LoanService loanService;

    @MockBean
    private RepositoryLoan repositoryLoan;

    @BeforeEach
    public void setUp(){
        this.loanService = new LoanServiceImpl(repositoryLoan);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo.")
    public void saveLoanTest(){
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan loan = Loan.builder()
                .customer(customer)
                .book(book)
                .build();

        Loan savedLoan = Loan.builder()
                .id(1l)
                .customer(customer)
                .book(book)
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(repositoryLoan.save(loan)).thenReturn( savedLoan );

        // execução
        Loan returnedLoan = loanService.save(loan);

        // verificação
        assertThat(savedLoan.getId()).isEqualTo(returnedLoan.getId());
        assertThat(savedLoan.getBook().getId()).isEqualTo(returnedLoan.getBook().getId());
        assertThat(savedLoan.getCustomer()).isEqualTo(returnedLoan.getCustomer());
        assertThat(savedLoan.getLoanDate()).isEqualTo(returnedLoan.getLoanDate());
    }
}
