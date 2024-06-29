package pl.dk.libraryapp.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.customer.dtos.CustomerDto;
import pl.dk.libraryapp.exceptions.CustomerAlreadyExistsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("It should save customer in database")
    void itShouldSaveCustomerInDatabase() {
        // Given
        String email = "john.doe@test.pl";
        CustomerDto customerDto = CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .build();

        Customer customer = Customer.builder()
                .firstName(customerDto.firstName())
                .lastName(customerDto.lastName())
                .email(email)
                .build();

        Customer customerWithId = Customer.builder()
                .id("1")
                .firstName(customerDto.firstName())
                .lastName(customerDto.lastName())
                .email(email)
                .build();

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenReturn(customerWithId);

        // When
        CustomerDto savedCustomerDto = underTest.saveCustomer(customerDto);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findByEmail(email),
                () -> verify(customerRepository, times(1)).save(customer),
                () -> assertNotNull(savedCustomerDto.id()),
                () -> assertEquals(customerDto.firstName(), savedCustomerDto.firstName()),
                () -> assertEquals(customerDto.lastName(), savedCustomerDto.lastName()),
                () -> assertEquals(customerDto.email(), savedCustomerDto.email())
        );
    }

    @Test
    @DisplayName("It should throw CustomerAlreadyExistsException when user already exists in database")
    void itShouldThrowCustomerAlreadyExistsExceptionWhenUserAlreadyExistsInDatabase() {
        // Given
        String email = "john.doe@test.pl";
        CustomerDto customerDto = CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .build();

        Customer customerWithId = Customer.builder()
                .id("1")
                .firstName(customerDto.firstName())
                .lastName(customerDto.lastName())
                .email(email)
                .build();

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customerWithId));

        // When & Then
        assertThrows(CustomerAlreadyExistsException.class, () -> underTest.saveCustomer(customerDto));

    }
}