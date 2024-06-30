package pl.dk.libraryapp.bookloan;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.customer.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanRepository extends MongoRepository<BookLoan, String> {

    Optional<BookLoan> findByBook_Id(String bookId);

    List<BookLoan> findAllByCustomer_Id(String customerId);

    int countBookLoanByBookEquals(Book book);
}
