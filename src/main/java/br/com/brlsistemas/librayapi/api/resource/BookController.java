package br.com.brlsistemas.librayapi.api.resource;

import br.com.brlsistemas.librayapi.api.dto.BookDTO;
import br.com.brlsistemas.librayapi.api.dto.LoanDTO;
import br.com.brlsistemas.librayapi.model.entity.Book;
import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.service.BookService;
import br.com.brlsistemas.librayapi.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("Book API")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a book")
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book book = modelMapper.map(dto, Book.class);
        Book savedBook = bookService.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @GetMapping("{id}")
    @ApiOperation("Obtains a book details by id")
    public BookDTO get(@PathVariable Long id) {
        return bookService
                .getById(id)
                .map( book -> modelMapper.map(book, BookDTO.class) )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletes a book by id")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Book successfully deleted")
    })
    public void delete(@PathVariable Long id) {
        Book book = bookService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        bookService.delete(book);
    }

    @PutMapping("{id}")
    @ApiOperation("Updates a book")
    public BookDTO update(@PathVariable Long id, BookDTO bookDTO) {
        return bookService.getById(id).map( book -> {
            book.setAuthor(bookDTO.getAuthor());
            book.setTitle(bookDTO.getTitle());
            book = bookService.update(book);
            return modelMapper.map(book, BookDTO.class);
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @GetMapping
    @ApiOperation("Find book by params")
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest ) {
        Book filter = modelMapper.map(bookDTO, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);

        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>( list, pageRequest, result.getTotalElements() );
    }

    @GetMapping("{id}/loans")
    @ApiOperation("Find loans by book")
    public Page<LoanDTO> loansByBook( @PathVariable Long id, Pageable pageable ){
        Book book = bookService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        Page<Loan> result = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {

                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;

                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }
}
