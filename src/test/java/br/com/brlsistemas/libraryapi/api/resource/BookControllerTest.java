package br.com.brlsistemas.libraryapi.api.resource;

import br.com.brlsistemas.libraryapi.api.dto.BookDTO;
import br.com.brlsistemas.libraryapi.api.dto.BookUpdateDTO;
import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.exception.BusinessException;
import br.com.brlsistemas.libraryapi.service.BookService;
import br.com.brlsistemas.libraryapi.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {
        // cen??rio  (given)
        // Simula o comportamento do m??todo save do service com a inst??ncia book.
        Book savedBook = Book.builder().id(10L).author("Anderson").title("As aventuras").isbn("123456").build();
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

        // execu????o (when)
        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lan??ar erro de valida????o quando n??o houver dados suficiente para cria????o do livro. (valida????o de integridade)")
    public void createInvalidBookTest() throws Exception {
        BookDTO dto = new BookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(3)) );
    }

    @Test
    @DisplayName("Deve lan??ar erro ao tentar cadastrar um livro com Isbn j?? utilizado por outro livro. (valida????o de regra de neg??cio)")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagemErro = "Isbn j?? cadastrado.";

        BDDMockito.given(bookService.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)) )
                .andExpect( jsonPath("errors[0]").value(mensagemErro) );
    }

    @Test
    @DisplayName("Deve obter informa????es de um livro")
    public void getBookDetailsTest() throws Exception {
        // cen??rio  (given)
        Long id = 1L;

        Book book = Book.builder()
                        .id(id)
                        .author(createNewBookDTO().getAuthor())
                        .title(createNewBookDTO().getTitle())
                        .isbn(createNewBookDTO().getIsbn())
                        .build();

        BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isOk() )
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBookDTO().getTitle()))
                .andExpect(jsonPath("author").value(createNewBookDTO().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBookDTO().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o livro procurado n??o existir")
    public void bookNotFoundTest() throws Exception {
        // cen??rio  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception {
        // cen??rio  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/1"));

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isNoContent() );
    }

    @Test
    @DisplayName("Deve retornar resource not found quando n??o encontrar o livro para deletar")
    public void deleteNonexistentBookTest() throws Exception {
        // cen??rio  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/1"));

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {
        // cen??rio  (given)
        Long id = 1L;

        String json = new ObjectMapper().writeValueAsString( createNewBookUpdateDTO() );

        Book updatingBook = Book.builder().id(1L).title("Titulo 1").author("Ander").isbn("123").build();
        BDDMockito.given(bookService.getById(id))
                .willReturn( Optional.of(updatingBook) );

        Book updatedBook = Book.builder().id(1L).title(createNewBookDTO().getTitle()).author(createNewBookDTO().getAuthor()).isbn(createNewBookDTO().getIsbn()).build();
        BDDMockito.given(bookService.update( Mockito.any() ))
                .willReturn( updatedBook );

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isOk() )
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBookDTO().getTitle()))
                .andExpect(jsonPath("author").value(createNewBookDTO().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBookDTO().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando nao encontrar o livro para atualizar")
    public void updateNonexistentBookTest() throws Exception {
        // cen??rio  (given)
        String json = new ObjectMapper().writeValueAsString( createNewBookUpdateDTO() );

        BDDMockito.given( bookService.getById(Mockito.anyLong()) )
                .willReturn( Optional.empty() );

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve filtrar livros")
    public void findBooksTest() throws Exception {
        // cen??rio
        Long id = 1L;
        Book book = Book.builder()
                            .id(id)
                            .title(createNewBookDTO().getTitle())
                            .author(createNewBookDTO().getAuthor())
                            .isbn(createNewBookDTO().getIsbn())
                            .build();

        BDDMockito.given( bookService.find( Mockito.any(Book.class), Mockito.any(Pageable.class) ) )
                .willReturn(new PageImpl<>(Arrays.asList(book), PageRequest.of(0, 100), 1) );

        String queryString = String.format("?title=%s&author=%s&page=0&size=100",
                book.getTitle(), book.getAuthor());

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        // verifica????o (then)
        mvc.perform(request)
                .andExpect( status().isOk() )
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    private static BookDTO createNewBookDTO() {
        return BookDTO.builder().author("Anderson").title("As aventuras").isbn("123456").build();
    }

    private static BookUpdateDTO createNewBookUpdateDTO() {
        return BookUpdateDTO.builder().author("Anderson").title("As aventuras").build();
    }
}
