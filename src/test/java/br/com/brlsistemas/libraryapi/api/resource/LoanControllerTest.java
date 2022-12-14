package br.com.brlsistemas.libraryapi.api.resource;

import br.com.brlsistemas.libraryapi.api.dto.LoanCreateDTO;
import br.com.brlsistemas.libraryapi.api.dto.LoanDTO;
import br.com.brlsistemas.libraryapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.libraryapi.api.dto.ReturnedLoanDTO;
import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.entity.Loan;
import br.com.brlsistemas.libraryapi.service.BookService;
import br.com.brlsistemas.libraryapi.service.LoanService;
import br.com.brlsistemas.libraryapi.service.LoanServiceTest;
import br.com.brlsistemas.libraryapi.exception.BusinessException;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

    static String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    // o fulano vai pedir emprestado livro com isbn 123
    // encontrar livro atrav??s do isbn
    //  bookRepository.findByIsbn -> retorna um Optional<Book>
    // tabela com customer, livro, data e status
    //  loanRepository.save(loan)
    // *** o save sempre retorna o objeto salvo.
    // *** o get/find sempre retorna um Optional.
    @Test
    @DisplayName("Deve realizar um empr??stimo")
    public void createLoanTest() throws Exception {
        // cen??rio
        LoanCreateDTO loanDTO = LoanCreateDTO.builder().isbn("123").customer("Fulano").customerEmail("fulano@email.com").build();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        Book foundBook = Book.builder()
                .id(1l)
                .isbn("123")
                .build();

        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn( Optional.of(foundBook) );

        Loan loan = Loan.builder().id(1l).customer("Anderson").book(foundBook).loanDate(LocalDate.now()).build();
        BDDMockito.given( loanService.save(Mockito.any(Loan.class)) ).willReturn( loan );

        // execu????o
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o
        mockMvc.perform( request )
                .andExpect( status().isCreated() )
                .andExpect( content().string("1") );
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer empr??stimo de um livro emprestado.")
    public void invalidIsbnCreateLoanTest() throws Exception {
        // cen??rio
        LoanCreateDTO loanDTO = LoanCreateDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        Book foundBook = Book.builder()
                .id(1l)
                .isbn("123")
                .build();

        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn( Optional.of(foundBook) );

        BDDMockito.given( loanService.save(Mockito.any(Loan.class)) ).willThrow(new BusinessException("Book already loaned"));

        // execu????o
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o
        mockMvc.perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)) )
                .andExpect( jsonPath("errors[0]").value("Book already loaned") );
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer empr??stimo de livro inexistente.")
    public void loanedBookErrorOnCreateLoanTest() throws Exception {
        // cen??rio
        LoanCreateDTO loanDTO = LoanCreateDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn( Optional.empty() );

        // execu????o
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o
        mockMvc.perform( request )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)) )
                .andExpect( jsonPath("errors[0]").value("Book not found for passed Isbn") );
    }

    @Test
    @DisplayName("Deve retornar um livro.")
    public void returnBookTest() throws Exception {
        // cen??rio
        ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
        String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

        Loan loan = Loan.builder().id(1l).build();
        BDDMockito.given( loanService.getById(Mockito.anyLong()) ).willReturn( Optional.of(loan) );

        // execu????o
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(LOAN_API.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o
        mockMvc.perform( request )
                .andExpect( status().isOk() );

        Mockito.verify(loanService, Mockito.times(1)).update(loan);
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente.")
    public void returnInexistentBookTest() throws Exception {
        // cen??rio
        ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
        String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

        BDDMockito.given( loanService.getById(Mockito.anyLong()) ).willReturn( Optional.empty() );

        // execu????o
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(LOAN_API.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verifica????o
        mockMvc.perform( request )
                .andExpect( status().isNotFound() );

        Mockito.verify(loanService, Mockito.never()).update( Mockito.any(Loan.class) );
    }
    @Test
    @DisplayName("Deve filtrar empr??stimos")
    public void findLoansTest() throws Exception{
        //cen??rio
        Long id = 1l;
        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);
        Book book = Book.builder().id(1l).isbn("321").build();
        loan.setBook(book);

        BDDMockito.given( loanService.find( Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Loan>( Arrays.asList(loan), PageRequest.of(0,10), 1 ) );

        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10",
                book.getIsbn(), loan.getCustomer());

        // execu????o (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        // verifica????o (then)
        mockMvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("content", Matchers.hasSize(1)))
                .andExpect( jsonPath("totalElements").value(1) )
                .andExpect( jsonPath("pageable.pageSize").value(10) )
                .andExpect( jsonPath("pageable.pageNumber").value(0))
        ;
    }
}
