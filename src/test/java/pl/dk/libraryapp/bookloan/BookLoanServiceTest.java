package pl.dk.libraryapp.bookloan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.book.Book;
import pl.dk.libraryapp.book.BookRepository;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;
import pl.dk.libraryapp.customer.Customer;
import pl.dk.libraryapp.customer.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookLoanServiceTest {

    @Mock
    private BookLoanRepository bookLoanRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BookRepository bookRepository;
    private AutoCloseable autoCloseable;
    private BookLoanService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new BookLoanServiceImpl(bookLoanRepository, bookRepository, customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("It should save book loan")
    void itShouldSaveBookLoan() {
        // Given
        SaveBookLoanDto saveBookLoanDto = SaveBookLoanDto.builder()
                .id("1")
                .bookId("1")
                .customerId("1")
                .build();

        Customer customer = Customer.builder()
                .id("1").build();
        Book book = Book.builder()
                .id("1").build();

        BookLoan bookLoan = BookLoan.builder()
                .id("1")
                .borrowedAt(LocalDateTime.now())
                .book(book)
                .customer(customer)
                .build();

        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));
        when(bookLoanRepository.save(any(BookLoan.class))).thenReturn(bookLoan);

        // When
        BookLoanDto result = underTest.saveBookLoan(saveBookLoanDto);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findById("1"),
                () -> verify(bookRepository, times(1)).findById("1"),
                () -> verify(bookLoanRepository, times(1)).save(any(BookLoan.class)),
                () -> assertNotNull(result.id()),
                () -> assertNotNull(result.borrowedAt()),
                () -> assertNull(result.returnedAt()),
                () -> assertEquals(saveBookLoanDto.bookId(), result.bookId()),
                () -> assertEquals(saveBookLoanDto.customerId(), result.customerId())
        );
    }

    @Test
    @DisplayName("It should set book loan returned date")
    void itShouldSetBookLoanReturnedDate() {
        // Given
        String bookLoanId = "1";

        Customer customer = Customer.builder()
                .id("1").build();
        Book book = Book.builder()
                .id("1").build();

        BookLoan bookLoan = BookLoan.builder()
                .id("1")
                .borrowedAt(LocalDateTime.now())
                .book(book)
                .customer(customer)
                .build();

        when(bookLoanRepository.findById(bookLoanId)).thenReturn(Optional.of(bookLoan));

        // When
        underTest.setBookLoanReturnedTime(bookLoanId);

        // Then
        ArgumentCaptor<BookLoan> bookLoanArgumentCaptor = ArgumentCaptor.forClass(BookLoan.class);
        assertAll(
                () -> verify(bookLoanRepository, times(1)).findById(bookLoanId),
                () -> verify(bookLoanRepository, times(1)).save(bookLoanArgumentCaptor.capture()),
                () -> assertNotNull(bookLoanArgumentCaptor.getValue().returnedAt())
        );
    }

    @Test
    @DisplayName("It should retrieve Customers book loan history")
    void itShouldRetrieveCustomersBookLoanHistory() {
        // Given
        String customerId = "1";
        int pageNumber = 1;
        int pageSize = 1;

        Customer customer = Customer.builder()
                .id("1").build();
        Book book = Book.builder()
                .id("1").build();

        BookLoan bookLoan = BookLoan.builder()
                .id("2")
                .borrowedAt(LocalDateTime.now().minusDays(1))
                .returnedAt(LocalDateTime.now())
                .book(book)
                .customer(customer)
                .build();

        when(bookLoanRepository.findAllByCustomer_Id(customerId)).thenReturn(List.of(bookLoan));

        // When
        List<BookLoanDto> result = underTest.findCustomerBookLoans(customerId, pageNumber, pageSize);

        // Then
        assertAll(
                () -> assertEquals(1, result.size())
        );
    }


}