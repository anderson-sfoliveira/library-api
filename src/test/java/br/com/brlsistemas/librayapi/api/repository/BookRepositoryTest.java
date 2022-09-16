package br.com.brlsistemas.librayapi.api.repository;

import br.com.brlsistemas.librayapi.api.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // utiliza o h2 como banco de dados em memória
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado.")
    public void returnTrueWhenIsbnExists(){
        // cenário
        String isbn = "123";

        // entityManager irá persistir o book no banco de dados h2.
        Book book = Book.builder().author("Anderson").title("As aventuras").isbn(isbn).build();
        entityManager.persist(book);

        // execução
        boolean existsByIsbn = bookRepository.existsByIsbn(isbn);

        // verificação
        assertThat(existsByIsbn).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o ISBN informado.")
    public void returnFalseWhenIsbnDoesntExists(){
        // cenário
        String isbn = "123";

        // execução
        boolean existsByIsbn = bookRepository.existsByIsbn(isbn);

        // verificação
        assertThat(existsByIsbn).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        // cenário
        String isbn = "123";

        // entityManager irá persistir o book no banco de dados h2.
        Book book = Book.builder().author("Anderson").title("As aventuras").isbn(isbn).build();
        entityManager.persist(book);

        // execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // verificação
        assertThat(foundBook.isPresent()).isTrue();
    }
}
