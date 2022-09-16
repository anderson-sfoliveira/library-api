package br.com.brlsistemas.librayapi.api.repository;

import br.com.brlsistemas.librayapi.api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
