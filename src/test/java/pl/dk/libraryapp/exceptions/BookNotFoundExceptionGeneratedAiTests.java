package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookNotFoundExceptionGeneratedAiTests {

    @Test
    void testConstructor() {
        // GIVEN
        String message = "Book not found";
        // WHEN
        BookNotFoundException exception = new BookNotFoundException(message);
        // THEN
        assertEquals(message, exception.getMessage());
    }
}