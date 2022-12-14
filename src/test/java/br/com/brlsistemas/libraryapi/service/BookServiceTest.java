package br.com.brlsistemas.libraryapi.service;

import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.repository.BookRepository;
import br.com.brlsistemas.libraryapi.service.impl.BookServiceImpl;
import br.com.brlsistemas.libraryapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest(){
        // cenário
        Long id = 1l;

        Book book = createValidBook();
        book.setId(id);

        Mockito.when( bookRepository.findById(id) ).thenReturn( Optional.of(book) );

        // execução
        Optional<Book> foundBook = bookService.getById(id);

        // verificações
        assertThat( foundBook.isPresent() ).isTrue();
        assertThat( foundBook.get().getId() ).isEqualTo(id);
        assertThat( foundBook.get().getAuthor() ).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getTitle() ).isEqualTo(book.getTitle());
        assertThat( foundBook.get().getIsbn() ).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe")
    public void bookNotFoundByIdTest(){
        // cenário
        Long id = 1l;

        Mockito.when( bookRepository.findById(id) ).thenReturn( Optional.empty() );

        // execução
        Optional<Book> book = bookService.getById(id);

        // verificações
        assertThat( book.isPresent() ).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteTest(){
        // cenário
        Book book = createValidBook();
        book.setId(1l);

        // execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> bookService.delete(book));

        // verificações
        Mockito.verify( bookRepository, Mockito.times(1) ).delete(book);
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar deletar um livro sem id")
    public void shouldNotDeleteABookWithoutId(){
        // cenário
        Book book = createValidBook();

        // execução
        Throwable exception = Assertions.catchThrowable(() -> bookService.delete(book));

        // verificações
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book id cant be null.");

        Mockito.verify( bookRepository, Mockito.never() ).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateTest(){
        // cenário
        Book book = createValidBook();
        book.setId(1l);

        // Simula o comportamento do método save do repository com a instância book.
        Book updatedBook = Book.builder()
                .id(1L)
                .isbn("11")
                .title("Outras aventuras")
                .author("Outro autor")
                .build();

        Mockito.when( bookRepository.save(book) ).thenReturn(updatedBook);

        // execução
        Book savedBook = bookService.update(book);

        // verificação
        assertThat(savedBook.getId()).isEqualTo(updatedBook.getId());
        assertThat(savedBook.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(savedBook.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(savedBook.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar atualizar um livro sem id")
    public void shouldNotUpdateABookWithoutId(){
        // cenário
        Book book = createValidBook();

        // execução
        Throwable exception = Assertions.catchThrowable(() -> bookService.update(book));

        // verificações
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book id cant be null.");

        Mockito.verify( bookRepository, Mockito.never() ).save(book);
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest(){
        // Cenário
        Book book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> lista = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);

        Mockito.when(bookRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        // Execução
        Page<Book> result = bookService.find(book, pageRequest);

        // Validação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void getBookByIsbnTest() {
        // cenário
        String isbn = "123456";
        Book book = createValidBook();
        book.setId(1l);

        Mockito.when( bookRepository.findByIsbn(isbn) ).thenReturn( Optional.of(book) );

        // execução
        Optional<Book> foundBook = bookService.getBookByIsbn(isbn);

        // verificações
        assertThat( foundBook.isPresent() ).isTrue();
        assertThat( foundBook.get().getId() ).isEqualTo(book.getId());
        assertThat( foundBook.get().getAuthor() ).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getTitle() ).isEqualTo(book.getTitle());
        assertThat( foundBook.get().getIsbn() ).isEqualTo(book.getIsbn());

        Mockito.verify( bookRepository, Mockito.times(1) ).findByIsbn(isbn);
    }

    private static Book createValidBook() {
        return Book.builder().author("Anderson").title("As aventuras").isbn("123456").build();
    }
}
