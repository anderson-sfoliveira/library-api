package br.com.brlsistemas.libraryapi.model.repository;

import br.com.brlsistemas.libraryapi.model.entity.Book;
import br.com.brlsistemas.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = " select case when ( count(l.id) > 0 ) then true else false end " +
            " from Loan l where l.book = :book and ( l.returned is null or l.returned is not true )")
    boolean existsByBookAndNotReturned( @Param("book") Book book);

    @Query(value = " select l from Loan as l join l.book as b where b.isbn = :isbn or l.customer = :customer ")
    Page<Loan> findByBookIsbnOrCustomer( @Param("isbn") String isbn, @Param("customer") String customer, Pageable pageable);

    Page<Loan> findByBook(Book book, Pageable pageable);

    @Query(value = "select l from Loan as l where l.loanDate <= :threeDaysAgo  and ( l.returned is null or l.returned is not true )")
    List<Loan> findByLoanDateLessThanAndNotReturned( @Param("threeDaysAgo") LocalDate threeDaysAgo );
}
