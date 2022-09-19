package br.com.brlsistemas.librayapi.api.repository;

import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static br.com.brlsistemas.librayapi.api.repository.BookRepositoryTest.createNewBook;
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
}
