package br.com.brlsistemas.librayapi.api.service;

import br.com.brlsistemas.librayapi.api.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);
}
