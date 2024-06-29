package pl.dk.libraryapp.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.libraryapp.customer.dtos.CustomerDto;
import pl.dk.libraryapp.exceptions.CustomerAlreadyExistsException;
import pl.dk.libraryapp.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    private CustomerService underTest;
    private AutoCloseable autoCloseable;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        underTest = new CustomerService(customerRepository, objectMapper);
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

    @Test
    @DisplayName(("It should find Customer by given id"))
    void itShouldFindCustomerByGivenId() {
        // Given
        String customerId = "1";

        Customer customerWithId = Customer.builder()
                .id(customerId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerWithId));

        // When
        CustomerDto result = underTest.findCustomerById(customerId);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findById(customerId),
                () -> assertEquals(customerWithId.id(), result.id()),
                () -> assertEquals(customerWithId.email(), result.email()),
                () -> assertEquals(customerWithId.firstName(), result.firstName()),
                () -> assertEquals(customerWithId.lastName(), result.lastName())
        );
    }

    @Test
    @DisplayName(("It should throw CustomerNotFoundException when user wants to retrieve non existing Customer"))
    void itShouldThrowCustomerNotFoundExceptionWhenUserWantsToRetrieveNonExistingCustomer() {
        // Given
        String customerId = "1";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertAll(
                () -> assertThrows(CustomerNotFoundException.class, () -> underTest.findCustomerById(customerId)),
                () -> verify(customerRepository, times(1)).findById(customerId)
        );
    }

    @Test
    @DisplayName("It should find all customers")
    void itShouldFindAllCustomers() {
        // Given
        int page = 1;
        int size = 25;
        Customer customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();
        List<Customer> customers = List.of(customer);
        PageImpl<Customer> pageImpl = new PageImpl<>(customers);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        when(customerRepository.findAll(pageRequest)).thenReturn(pageImpl);

        // When
        List<CustomerDto> result = underTest.findAllCustomers(page, size);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findAll(pageRequest),
                () -> assertEquals(1, result.size()));
    }

    @Test
    @DisplayName("It should update Customer firstName ale lastName")
    void itShouldUpdateCustomerFirstNameAndCustomerLastName() throws JsonProcessingException {
        // Given
        String customerId = "1";

        Customer customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(any(Customer.class));
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        String jsonMergePatchUpdate = """
                {
                "firstName": "John - updated",
                "lastName": "Doe - updated"
                }
                """.trim();
        JsonMergePatch jsonMergePatch = objectMapper.readValue(jsonMergePatchUpdate, JsonMergePatch.class);

        // When
        underTest.updateCustomer(customerId, jsonMergePatch);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findById(customerId),
                () -> verify(customerRepository, times(1)).save(customerArgumentCaptor.capture()),
                () -> assertEquals("John - updated", customerArgumentCaptor.getValue().firstName()),
                () -> assertEquals("Doe - updated", customerArgumentCaptor.getValue().lastName())
        );
    }

    @Test
    @DisplayName("It should delete customer by given Id")
    void itShouldDeleteCustomerByGivenId() {
        // Given
        String customerId = "1";

        Customer customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        // When
        underTest.deleteCustomerById(customerId);

        // Then
        assertAll(
                () -> verify(customerRepository, times(1)).findById(customerId),
                () -> verify(customerRepository, times(1)).delete(customerArgumentCaptor.capture())
        );
    }

    @Test
    @DisplayName("It should throw CustomerNotFoundException when user tries to delete Customer with non existing Id")
    void itShouldThrowCustomerNotFoundExceptionWhenUserTriesToDeleteCustomerWithNonExistingId() {
        // Given
        String customerId = "1";

        Customer customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.pl")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertAll(
                () -> assertThrows(CustomerNotFoundException.class, () -> underTest.deleteCustomerById(customerId)),
                () -> verify(customerRepository, times(1)).findById(customerId)
        );
    }
}