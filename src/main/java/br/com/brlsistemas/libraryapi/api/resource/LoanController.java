package br.com.brlsistemas.libraryapi.api.resource;

import br.com.brlsistemas.libraryapi.api.dto.*;
import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.entity.Loan;
import br.com.brlsistemas.libraryapi.service.BookService;
import br.com.brlsistemas.libraryapi.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a loan")
    public Long createLoan(@RequestBody LoanCreateDTO loanDTO){
        Book book = bookService.getBookByIsbn(loanDTO.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed Isbn"));

        Loan loan = Loan.builder()
                .customer(loanDTO.getCustomer())
                .book(book)
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = loanService.save(loan);
        return savedLoan.getId();
    }

    @PatchMapping("{id}")
    @Operation(summary = "Return loaned book")
    public void returnBook( @PathVariable Long id, @RequestBody ReturnedLoanDTO returnedLoanDTO ) {
        Loan loan = loanService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        loan.setReturned(returnedLoanDTO.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    @Operation(summary = "Find loan by params")
    public Page<LoanDTO> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
        Page<Loan> result = loanService.find(loanFilterDTO, pageable);
        List<LoanDTO> loans = result
                .getContent()
                .stream()
                .map(entity -> {

                    Book book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;

                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(loans, pageable, result.getTotalElements());
    }
}
