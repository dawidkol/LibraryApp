package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerAlreadyExistsExceptionGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String message = "Customer already exists";
        // WHEN
        CustomerAlreadyExistsException exception = new CustomerAlreadyExistsException(message);
        // THEN
        assertEquals(message, exception.getMessage());
    }
}