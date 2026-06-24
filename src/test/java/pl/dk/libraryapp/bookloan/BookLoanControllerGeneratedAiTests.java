package pl.dk.libraryapp.bookloan;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.bookloan.dtos.BookLoanDto;
import pl.dk.libraryapp.bookloan.dtos.SaveBookLoanDto;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookLoanControllerGeneratedAiTests {

    @InjectMocks
    private BookLoanController bookLoanController;

    @Mock
    private BookLoanService bookLoanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_success() {
        // GIVEN
        SaveBookLoanDto saveBookLoanDto = SaveBookLoanDto.builder()
                .id("123")
                .bookId("book1")
                .customerId("customer1")
                .build();
        BookLoanDto bookLoanDto = BookLoanDto.builder()
                .id("456")
                .borrowedAt(LocalDateTime.now())
                .returnedAt(null)
                .bookId("book1")
                .customerId("customer1")
                .build();

        when(bookLoanService.saveBookLoan(saveBookLoanDto)).thenReturn(bookLoanDto);

        // WHEN
        ResponseEntity<BookLoanDto> response = bookLoanController.borrowBook(saveBookLoanDto);

        // THEN
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("456", response.getBody().id());
    }

    @Test
    void returnBook_success() {
        // GIVEN
        String id = "789";

        // WHEN
        ResponseEntity<?> response = bookLoanController.returnBook(id);

        // THEN
        assertEquals(204, response.getStatusCodeValue());
        verify(bookLoanService).setBookLoanReturnedTime(id);
    }

    @Test
    void retrieveUserHistory_success() {
        // GIVEN
        String userId = "user1";
        int page = 0;
        int size = 10;
        List<BookLoanDto> bookLoans = List.of(BookLoanDto.builder().id("loan1").build());

        when(bookLoanService.findCustomerBookLoans(userId, page, size)).thenReturn(bookLoans);

        // WHEN
        ResponseEntity<List<BookLoanDto>> response = bookLoanController.retrieveUserHistory(userId, page, size);

        // THEN
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }


}