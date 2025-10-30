package pl.dk.libraryapp.customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String id = "abc123";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        // WHEN
        Customer customer = new Customer(id, firstName, lastName, email);
        // THEN
        assertEquals(id, customer.id());
        assertEquals(firstName, customer.firstName());
        assertEquals(lastName, customer.lastName());
        assertEquals(email, customer.email());
    }

    @Test
    void testToString() {
        // GIVEN
        String id = "abc123";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        Customer customer = new Customer(id, firstName, lastName, email);
        // WHEN
        String toStringResult = customer.toString();
        // THEN
        assertTrue(toStringResult.contains(id));
        assertTrue(toStringResult.contains(firstName));
        assertTrue(toStringResult.contains(lastName));
        assertTrue(toStringResult.contains(email));
    }

}