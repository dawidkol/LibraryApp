package pl.dk.libraryapp.customer;

import org.junit.jupiter.api.Test;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDtoMapperTest {

    @Test
    void itShouldMapCustomerToCustomerDto() {
        // Given
        CustomerDto customerDto = CustomerDto.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        Customer customerWithId = Customer.builder()
                .id("1")
                .firstName(customerDto.firstName())
                .lastName(customerDto.lastName())
                .email(customerDto.email())
                .build();

        // When
        Customer customer = CustomerDtoMapper.map(customerDto);

        // Then
        assertAll(
                () -> assertNotNull(customer.id()),
                () -> assertEquals(customerDto.firstName(), customerDto.firstName()),
                () -> assertEquals(customerDto.lastName(), customer.lastName()),
                () -> assertEquals(customerDto.email(), customer.email())
        );
    }
}