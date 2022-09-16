package br.com.brlsistemas.librayapi.api.resource;

import br.com.brlsistemas.librayapi.api.dto.BookDTO;
import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.exception.ApiErrors;
import br.com.brlsistemas.librayapi.api.service.BookService;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

//    @Autowired
    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book book = modelMapper.map(dto, Book.class);
        Book savedBook = bookService.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex) {
        return new ApiErrors(ex);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public BookDTO create(@RequestBody BookDTO dto) {
//        Book book = Book.builder()
//                        .author(dto.getAuthor())
//                        .title(dto.getTitle())
//                        .isbn(dto.getIsbn())
//                        .build();
//
//        Book savedBook = bookService.save(book);
//
//        return BookDTO.builder()
//                .id(savedBook.getId())
//                .author(savedBook.getAuthor())
//                .title(savedBook.getTitle())
//                .isbn(savedBook.getIsbn())
//                .build();
//    }
}
