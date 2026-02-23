package pl.dk.libraryapp.customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.libraryapp.customer.dtos.CustomerDto;
import pl.dk.libraryapp.exceptions.CustomerAlreadyExistsException;
import pl.dk.libraryapp.exceptions.CustomerNotFoundException;
import pl.dk.libraryapp.customer.CustomerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CustomerServiceImplGeneratedAiTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCustomer_ExistingEmailThrowsException() {
        // GIVEN
        CustomerDto customerDto = new CustomerDto("John", "Doe", "johndoe@example.com");
        when(customerRepository.findByEmail(customerDto.email())).thenReturn(new Customer("id1", "John", "Doe", "johndoe@example.com"));

        // WHEN
        try {
            customerService.saveCustomer(customerDto);
            fail(); // Should throw exception
        } catch (CustomerAlreadyExistsException e) {
            // THEN
            assertEquals("Customer with email johndoe@example.com already exists", e.getMessage());
        }
    }

    @Test
    void testSaveCustomer_NewEmailSavesSuccessfully() {
        // GIVEN
        CustomerDto customerDto = new CustomerDto("Jane", "Doe", "janedoe@example.com");
        when(customerRepository.findByEmail(customerDto.email())).thenReturn(null);

        // WHEN
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);

        // THEN
        assertNotNull(savedCustomer);
    }

    @Test
    void testFindCustomerById_ExistingIdReturnsCustomer() {
        // GIVEN
        CustomerDto expectedCustomer = new CustomerDto("John", "Doe", "johndoe@example.com");
        when(customerRepository.findById("id1")).thenReturn(Optional.of(new Customer("id1", "John", "Doe", "johndoe@example.com")));

        // WHEN
        CustomerDto actualCustomer = customerService.findCustomerById("id1");

        // THEN
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    void testFindCustomerById_NonExistingIdThrowsException() {
        // GIVEN
        when(customerRepository.findById("id2")).thenReturn(Optional.empty());

        // WHEN
        try {
            customerService.findCustomerById("id2");
            fail(); // Should throw exception
        } catch (CustomerNotFoundException e) {
            // THEN
            assertEquals("Customer with id id2 not found", e.getMessage());
        }
    }

    @Test
    void testFindAllCustomers_ReturnsList() {
        // GIVEN
        when(customerRepository.findAll(PageRequest.of(0, 10))).thenReturn(List.of(new Customer("id1", "John", "Doe", "johndoe@example.com")));

        // WHEN
        List<CustomerDto> customers = customerService.findAllCustomers(0, 10);

        // THEN
        assertEquals(1, customers.size());
    }


    @Test
    void testUpdateCustomer_SuccessfulPatch() {
        // GIVEN
        CustomerDto existingCustomer = new CustomerDto("John", "Doe", "johndoe@example.com");
        when(customerRepository.findById("id1")).thenReturn(Optional.of(new Customer("id1", "John", "Doe", "johndoe@example.com")));

        // WHEN
        customerService.updateCustomer("id1", JsonMergePatch.fromJsonString("{\"firstName\":\"Jane\"}"));

        // THEN
        verify(customerRepository).save(any());
    }