package br.com.brlsistemas.librayapi.api.service;

import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.repository.BookRepository;
import br.com.brlsistemas.librayapi.api.service.impl.BookServiceImpl;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;
    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp(){
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        // cenário
        Book book = createValidBook();

        // Simula o comportamento do método save do repository com a instância book.
        Mockito.when( bookRepository.save(book) ).thenReturn(Book.builder()
                .id(1L)
                .isbn("123456")
                .title("As aventuras")
                .author("Anderson")
                .build());

        Mockito.when( bookRepository.existsByIsbn(Mockito.anyString()) ).thenReturn( false );

        // execução
        Book savedBook = bookService.save(book);

        // verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getAuthor()).isEqualTo("Anderson");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getIsbn()).isEqualTo("123456");
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        // cenário
        Book book = createValidBook();

        // Simula o comportamento do método existsByIsbn do bookRepository que é chamado no bookService.save.
        Mockito.when( bookRepository.existsByIsbn(Mockito.anyString()) ).thenReturn( true );

        // execução
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        // verificações
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify( bookRepository, Mockito.never() ).save(book);
    }

    private static Book createValidBook() {
        return Book.builder().author("Anderson").title("As aventuras").isbn("123456").build();
    }
}
