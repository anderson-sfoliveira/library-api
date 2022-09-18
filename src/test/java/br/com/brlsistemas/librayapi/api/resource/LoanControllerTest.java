package br.com.brlsistemas.librayapi.api.resource;

import br.com.brlsistemas.librayapi.api.dto.LoanDTO;
import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.entity.Loan;
import br.com.brlsistemas.librayapi.api.service.BookService;
import br.com.brlsistemas.librayapi.api.service.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private LoanService loanService;

    @Test
    @DisplayName("Deve realizar um empréstimo")
    public void createLoanTest() throws Exception {
        // cenário
        LoanDTO loanDTO = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        Book foundBook = Book.builder()
                .id(1l)
//                .title("As aventuras") informações não necessárias nesse cenário
//                .author("Fulano")
                .isbn("123")
                .build();

        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn( Optional.of(foundBook) );

        Loan loan = Loan.builder().id(1l).customer("Anderson").book(foundBook).loanDate(LocalDate.now()).build();
        BDDMockito.given( loanService.save(Mockito.any(Loan.class)) ).willReturn( loan );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform( request )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").value(1l) )
                ;


        // o fulano vai pedir emprestado livro com isbn 123
        // encontrar livro através do isbn
            //  bookRepository.findByIsbn -> retorna um Optional<Book>
        // tabela com customer, livro, data e status
            //  loanRepository.save(loan)

        // *** o save sempre retorna o objeto salvo.
        // *** o get/find sempre retorna um Optional.
    }
}
