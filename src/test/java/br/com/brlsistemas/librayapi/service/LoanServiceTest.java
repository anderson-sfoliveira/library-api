package br.com.brlsistemas.librayapi.service;

import br.com.brlsistemas.librayapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.librayapi.model.entity.Book;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.model.repository.LoanRepository;
import br.com.brlsistemas.librayapi.service.impl.LoanServiceImpl;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    private LoanService loanService;

    @MockBean
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp(){
        this.loanService = new LoanServiceImpl(loanRepository);
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

        Mockito.when( loanRepository.existsByBookAndNotReturned(book) ).thenReturn( false );

        Mockito.when(loanRepository.save(loan)).thenReturn( savedLoan );

        // execução
        Loan returnedLoan = loanService.save(loan);

        // verificação
        assertThat(savedLoan.getId()).isEqualTo(returnedLoan.getId());
        assertThat(savedLoan.getBook().getId()).isEqualTo(returnedLoan.getBook().getId());
        assertThat(savedLoan.getCustomer()).isEqualTo(returnedLoan.getCustomer());
        assertThat(savedLoan.getLoanDate()).isEqualTo(returnedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado.")
    public void loanedSaveLoanTest(){
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan loan = Loan.builder()
                .customer(customer)
                .book(book)
                .build();

        Mockito.when( loanRepository.existsByBookAndNotReturned(book) ).thenReturn( true );

        // execução
        Throwable exception = Assertions.catchThrowable(() -> loanService.save(loan));

        // verificações
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        Mockito.verify(loanRepository, Mockito.never() ).save(loan);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo ID")
    public void getLoanDetailsTest() {
        // cenário
        Loan loan = createLoan();
        Long id = 1l;
        loan.setId(id);

        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        // execução
        Optional<Loan> foundLoan = loanService.getById(id);

        // verificações
        assertThat(foundLoan.isPresent()).isTrue();
        assertThat(foundLoan.get().getId()).isEqualTo(id);
        assertThat(foundLoan.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(foundLoan.get().getBook().getId()).isEqualTo(loan.getBook().getId());
        assertThat(foundLoan.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        Mockito.verify(loanRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo.")
    public void updateLoanTest(){
        // cenário
        Loan loan = createLoan();
        Long id = 1l;
        loan.setId(id);

        Mockito.when(loanRepository.save(loan)).thenReturn( loan );

        // execução
        Loan returnedLoan = loanService.update(loan);

        // verificação
        assertThat(returnedLoan.getReturned()).isTrue();
        Mockito.verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades")
    public void findLoanTest(){
        // Cenário
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder()
                .isbn("321")
                .customer("Fulano")
                .build();

        Loan loan = createLoan();
        loan.setId(1l);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());
        Mockito.when(loanRepository.findByBookIsbnOrCustomer(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(PageRequest.class))
                )
                .thenReturn(page);

        // Execução
        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        // Validação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1l).build();

        return Loan.builder()
                .customer("Fulano")
                .book(book)
                .loanDate(LocalDate.now())
                .returned(true)
                .build();
    }
}
