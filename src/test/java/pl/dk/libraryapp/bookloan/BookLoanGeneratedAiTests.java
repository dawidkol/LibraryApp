package pl.dk.libraryapp.bookloan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.libraryapp.book.Book; 
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookLoanGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testBookLoanConstructor() {
        // GIVEN
        String id = "12345";
        LocalDateTime borrowedAt = LocalDateTime.now();
        LocalDateTime returnedAt = LocalDateTime.now().plusDays(7);
        Book book = new Book("67890", "Title", "Author", "Publisher", "ISBN", true);
        Customer customer = new Customer("11122", "John", "Doe", "john.doe@example.com");

        // WHEN
        BookLoan bookLoan = new BookLoan(id, borrowedAt, returnedAt, book, customer);

        // THEN
        assertEquals(id, bookLoan.getId());
        assertEquals(borrowedAt, bookLoan.getBorrowedAt());
        assertEquals(returnedAt, bookLoan.getReturnedAt());
        assertEquals(book, bookLoan.getBook());
        assertEquals(customer, bookLoan.getCustomer());
    }

}