package pl.dk.libraryapp.exceptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class ServerExceptionGeneratedAiTests {
    @Test
    void testConstructor() {
        // GIVEN - 
        // WHEN -  
        ServerException exception = new ServerException();
        // THEN - Assert the expected properties of the exception object.
        assertNotNull(exception);
        assertEquals("Server error, try again later", exception.getMessage());
    }

}