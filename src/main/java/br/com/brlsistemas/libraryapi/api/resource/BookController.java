package br.com.brlsistemas.libraryapi.api.resource;

import br.com.brlsistemas.libraryapi.api.dto.BookDTO;
import br.com.brlsistemas.libraryapi.api.dto.BookUpdateDTO;
import br.com.brlsistemas.libraryapi.api.dto.LoanDTO;
import br.com.brlsistemas.libraryapi.api.exception.ApiErrors;
import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.entity.Loan;
import br.com.brlsistemas.libraryapi.service.BookService;
import br.com.brlsistemas.libraryapi.service.LoanService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j // log
//@OpenAPIDefinition("Book API")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrors.class)) }
            )
    })
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        log.info("Create a book for isbn: {}", dto.getIsbn());
        Book book = modelMapper.map(dto, Book.class);
        Book savedBook = bookService.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtains a book details by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrors.class)) }
            )
    })
    public BookDTO get(@PathVariable Long id) {
        log.info("Obtaining details for book id: {}", id);
        return bookService
                .getById(id)
                .map( book -> modelMapper.map(book, BookDTO.class) )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a book by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Book successfully deleted"
//                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrors.class)) }
            )
    })
    public void delete(@PathVariable Long id) {
        log.info("Deleting book of id: {}", id);
        Book book = bookService.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        bookService.delete(book);
    }

    @PutMapping("{id}")
    @Operation(summary = "Updates a book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrors.class)) }
            )
    })
    public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookUpdateDTO bookDTO) {
        log.info("updating book of id: {}", id);
        return bookService.getById(id).map( book -> {
            book.setAuthor(bookDTO.getAuthor());
            book.setTitle(bookDTO.getTitle());
            book = bookService.update(book);
            return modelMapper.map(book, BookDTO.class);
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @GetMapping
    @Operation(summary = "Find book by params")
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
    @Operation(summary = "Find loans by book")
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
