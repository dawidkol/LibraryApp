package pl.dk.libraryapp.bookloan.dtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BookLoanDtoGeneratedAiTests {

    private String id;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private String bookId;
    private String customerId;

    @BeforeEach
    void setUp() {
        id = "testId";
        borrowedAt = LocalDateTime.now();
        returnedAt = LocalDateTime.now().plusDays(1);
        bookId = "testBookId";
        customerId = "testCustomerId";
    }

    @Test
    void testConstructor_ValidData() {
        // GIVEN valid data for the BookLoanDto constructor
        BookLoanDto bookLoanDto = BookLoanDto.builder()
                .id(id)
                .borrowedAt(borrowedAt)
                .returnedAt(returnedAt)
                .bookId(bookId)
                .customerId(customerId)
                .build();

        // THEN the constructor builds a valid BookLoanDto object
        assert id == bookLoanDto.id();
        assert borrowedAt == bookLoanDto.borrowedAt();
        assert returnedAt == bookLoanDto.returnedAt();
        assert bookId == bookLoanDto.bookId();
        assert customerId == bookLoanDto.customerId();
    }

}