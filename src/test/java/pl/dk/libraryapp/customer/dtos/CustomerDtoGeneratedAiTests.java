package pl.dk.libraryapp.customer.dtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerDtoGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testConstructor() {
        // GIVEN
        String id = "123";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        // WHEN
        CustomerDto customerDto = CustomerDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
        // THEN
        assertEquals(id, customerDto.id());
        assertEquals(firstName, customerDto.firstName());
        assertEquals(lastName, customerDto.lastName());
        assertEquals(email, customerDto.email());
    }

}