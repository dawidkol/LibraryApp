package pl.dk.libraryapp.customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.libraryapp.customer.Customer;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDtoMapperGeneratedAiTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void mapCustomerToCustomerDto_HappyPath() {
        // GIVEN
        Customer customer = Customer.builder()
                .id("123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // WHEN
        CustomerDto customerDto = CustomerDtoMapper.map(customer);

        // THEN
        assertEquals("123", customerDto.id());
        assertEquals("John", customerDto.firstName());
        assertEquals("Doe", customerDto.lastName());
        assertEquals("john.doe@example.com", customerDto.email());
    }

    @Test
    void mapCustomerDtoToCustomer_HappyPath() {
        // GIVEN
        CustomerDto customerDto = CustomerDto.builder()
                .id("456")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        // WHEN
        Customer customer = CustomerDtoMapper.map(customerDto);

        // THEN
        assertEquals("456", customer.id());
        assertEquals("Jane", customer.firstName());
        assertEquals("Smith", customer.lastName());
        assertEquals("jane.smith@example.com", customer.email());
    }
}