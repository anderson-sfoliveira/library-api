package br.com.brlsistemas.librayapi.api.resource;

import br.com.brlsistemas.librayapi.api.dto.BookDTO;
import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import br.com.brlsistemas.librayapi.api.service.BookService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {
        // Simula o comportamento do método save do service com a instância book.
        Book savedBook = Book.builder().id(10L).author("Anderson").title("As aventuras").isbn("123456").build();
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro. (validação de integridade)")
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
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com Isbn já utilizado por outro livro. (validação de regra de negócio)")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagemErro = "Isbn já cadastrado.";

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
    @DisplayName("Deve obter informações de um livro")
    public void getBookDetailsTest() throws Exception {
        // cenário  (given)
        Long id = 1l;

        Book book = Book.builder()
                        .id(id)
                        .author(createNewBookDTO().getAuthor())
                        .title(createNewBookDTO().getTitle())
                        .isbn(createNewBookDTO().getIsbn())
                        .build();

        BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        // verificação (then)
        mvc.perform(request)
                .andExpect( status().isOk() )
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBookDTO().getTitle()))
                .andExpect(jsonPath("author").value(createNewBookDTO().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBookDTO().getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o livro procurado não existir")
    public void bookNotFoundTest() throws Exception {
        // cenário  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON);

        // verificação (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception {
        // cenário  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/1"));

        // verificação (then)
        mvc.perform(request)
                .andExpect( status().isNoContent() );
    }

    @Test
    @DisplayName("Deve retornar resource not found quando não encontrar o livro para deletar")
    public void deleteNonexistentBookTest() throws Exception {
        // cenário  (given)
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/1"));

        // verificação (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {
        // cenário  (given)
        Long id = 1l;

        String json = new ObjectMapper().writeValueAsString( createNewBookDTO() );

        Book updatingBook = Book.builder().id(1l).title("Titulo 1").author("Ander").isbn("123").build();
        BDDMockito.given(bookService.getById(id))
                .willReturn( Optional.of(updatingBook) );

        Book updatedBook = Book.builder().id(1l).title(createNewBookDTO().getTitle()).author(createNewBookDTO().getAuthor()).isbn(createNewBookDTO().getIsbn()).build();
        BDDMockito.given(bookService.update( Mockito.any() ))
                .willReturn( updatedBook );

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verificação (then)
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
        // cenário  (given)
        String json = new ObjectMapper().writeValueAsString( createNewBookDTO() );

        BDDMockito.given(bookService.getById( Mockito.anyLong() ))
                .willReturn( Optional.empty() );

        // execução (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verificação (then)
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    private static BookDTO createNewBookDTO() {
        return BookDTO.builder().author("Anderson").title("As aventuras").isbn("123456").build();
    }
}
