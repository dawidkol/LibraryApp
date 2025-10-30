package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookLoanReturnedExceptionGeneratedAiTests {

    @Test
    void testConstructor() {
        // GIVEN
        String message = "Book already returned";
        // WHEN
        BookLoanReturnedException exception = new BookLoanReturnedException(message);
        // THEN
        assertEquals(message, exception.getMessage());
    }
}