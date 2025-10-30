package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BookLoanNotFoundExceptionGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String message = "Book loan not found";
        // WHEN
        BookLoanNotFoundException exception = new BookLoanNotFoundException(message);
        // THEN
        assertEquals(message, exception.getMessage());
    }
}