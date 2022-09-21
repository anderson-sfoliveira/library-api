package br.com.brlsistemas.librayapi.api.resource;

import br.com.brlsistemas.librayapi.api.dto.BookDTO;
import br.com.brlsistemas.librayapi.api.dto.LoanDTO;
import br.com.brlsistemas.librayapi.api.dto.LoanFilterDTO;
import br.com.brlsistemas.librayapi.api.dto.ReturnedLoanDTO;
import br.com.brlsistemas.librayapi.model.entity.Book;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.service.BookService;
import br.com.brlsistemas.librayapi.service.LoanService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LoanController {

    private BookService bookService;
    private LoanService loanService;
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createLoan(@RequestBody LoanDTO loanDTO){
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
    public void returnBook( @PathVariable Long id, @RequestBody ReturnedLoanDTO returnedLoanDTO ) {
        Loan loan = loanService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        loan.setReturned(returnedLoanDTO.getReturned());
        loanService.update(loan);
    }

    @GetMapping
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
