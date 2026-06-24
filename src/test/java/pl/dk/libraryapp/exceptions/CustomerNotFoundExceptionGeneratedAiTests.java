package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerNotFoundExceptionGeneratedAiTests {

    @Test
    void testConstructor() {
        // GIVEN
        String expectedMessage = "Customer not found";
        // WHEN
        CustomerNotFoundException exception = new CustomerNotFoundException(expectedMessage);
        // THEN
        assertEquals(expectedMessage, exception.getMessage());
    }
}