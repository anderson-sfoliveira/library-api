package br.com.brlsistemas.librayapi.api.service.impl;

import br.com.brlsistemas.librayapi.api.entity.Book;
import br.com.brlsistemas.librayapi.api.repository.BookRepository;
import br.com.brlsistemas.librayapi.api.service.BookService;
import br.com.brlsistemas.librayapi.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado");
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {

    }
}
