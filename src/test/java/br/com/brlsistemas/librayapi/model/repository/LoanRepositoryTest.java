package br.com.brlsistemas.librayapi.model.repository;

import br.com.brlsistemas.librayapi.model.entity.Book;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static br.com.brlsistemas.librayapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // utiliza o h2 como banco de dados em memória
public class LoanRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LoanRepository loanRepository;

    @Test
    @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro.")
    public void existsByBookAndNotReturnedTest(){
        // cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .loanDate(LocalDate.now())
                .book(book)
                .customer("Fulano")
                .build();
        entityManager.persist(loan);

        // execução
        boolean exists = loanRepository.existsByBookAndNotReturned(book);

        // verificação
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomerTest(){
        // cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .loanDate(LocalDate.now())
                .book(book)
                .customer("Fulano")
                .build();
        entityManager.persist(loan);

        // execução
        Page<Loan> result = loanRepository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));

        // verificação
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve obter empréstimo cuja data de empréstimo for menor ou igual a 3 dias atrás e não retornados")
    public void findByLoanDateLessThanAndNotReturnedTest(){
        // cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .loanDate(LocalDate.now().minusDays(5))
                .book(book)
                .customer("Fulano")
                .build();
        entityManager.persist(loan);

        // execução
        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        // verificação
        assertThat(result).hasSize(1);
        assertThat(result).contains(loan);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver empréstimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest(){
        // cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder()
                .loanDate(LocalDate.now())
                .book(book)
                .customer("Fulano")
                .build();
        entityManager.persist(loan);

        // execução
        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        // verificação
        assertThat(result).isEmpty();
    }
}
