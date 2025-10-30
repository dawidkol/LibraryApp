package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BookBorrowedExceptionGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String message = "Book already borrowed";
        // WHEN
        BookBorrowedException exception = new BookBorrowedException(message);
        // THEN
        assertEquals(message, exception.getMessage());
    }
}