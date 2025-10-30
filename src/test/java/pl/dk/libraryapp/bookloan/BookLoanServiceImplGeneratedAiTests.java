package pl.dk.libraryapp.bookloan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.book.BookRepository;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;
import pl.dk.libraryapp.customer.Customer;
import pl.dk.libraryapp.customer.CustomerRepository;
import pl.dk.libraryapp.exceptions.BookBorrowedException;
import pl.dk.libraryapp.exceptions.BookNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookLoanServiceImplGeneratedAiTests {

    @InjectMocks
    private BookLoanServiceImpl bookLoanService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBookLoan_Success() {
        // GIVEN
        String bookId = "123";
        String customerId = "456";
        SaveBookLoanDto saveBookLoanDto = new SaveBookLoanDto(bookId, customerId, null);
        Book book = new Book("123", "Title", "Author", "Publisher", "ISBN", true);
        Customer customer = new Customer("456", "Name", "Email");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // WHEN
        BookLoanDto bookLoanDto = bookLoanService.saveBookLoan(saveBookLoanDto);

        // THEN
        assertNotNull(bookLoanDto);
        assertEquals("123", bookLoanDto.bookId());
        assertEquals("456", bookLoanDto.customerId());
        assertEquals(LocalDateTime.now(), bookLoanDto.borrowedAt());
        assertNull(bookLoanDto.returnedAt());
        verify(bookRepository).findById(bookId);
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testSaveBookLoan_BookNotFound() {
        // GIVEN
        String bookId = "123";
        String customerId = "456";
        SaveBookLoanDto saveBookLoanDto = new SaveBookLoanDto(bookId, customerId, null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(BookNotFoundException.class, () -> bookLoanService.saveBookLoan(saveBookLoanDto));

        // THEN
    }

    @Test
    void testSaveBookLoan_BookBorrowed() {
        // GIVEN
        String bookId = "123";
        String customerId = "456";
        SaveBookLoanDto saveBookLoanDto = new SaveBookLoanDto(bookId, customerId, null);
        Book book = new Book("123", "Title", "Author", "Publisher", "ISBN", false);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // WHEN
        assertThrows(BookBorrowedException.class, () -> bookLoanService.saveBookLoan(saveBookLoanDto));

        // THEN
    }


}