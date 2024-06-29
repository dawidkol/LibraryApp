package pl.dk.libraryapp.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDtoMapperTest {

    @Test
    @DisplayName("It should map CustomerDto to Customer")
    void itShouldMapCustomerDtoToCustomer() {
        // Given
        CustomerDto customerDto = CustomerDto.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        // When
        Customer customer = CustomerDtoMapper.map(customerDto);

        // Then
        assertAll(
                () -> assertNotNull(customer.id()),
                () -> assertEquals(customerDto.firstName(), customer.firstName()),
                () -> assertEquals(customerDto.lastName(), customer.lastName()),
                () -> assertEquals(customerDto.email(), customer.email())
        );
    }

    @Test
    @DisplayName("It should map CustomerDto to Customer")
    void itShouldMapCustomerToCustomerDto() {
        // Given
        Customer customerWithId = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        // When
        CustomerDto customerDto = CustomerDtoMapper.map(customerWithId);

        // Then
        assertAll(
                () -> assertNotNull(customerDto.id()),
                () -> assertEquals(customerWithId.firstName(), customerDto.firstName()),
                () -> assertEquals(customerWithId.lastName(), customerDto.lastName()),
                () -> assertEquals(customerWithId.email(), customerDto.email())
        );
    }
}