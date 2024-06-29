package pl.dk.libraryapp.customer;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.customer.dtos.CustomerDto;
import pl.dk.libraryapp.exceptions.CustomerAlreadyExistsException;
import pl.dk.libraryapp.exceptions.CustomerNotFoundException;

import java.util.List;


@Service
@AllArgsConstructor
class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        customerRepository.findByEmail(customerDto.email()).ifPresent(existingCustomer -> {
            throw new CustomerAlreadyExistsException(
                    "Customer with email %s already exists".formatted(customerDto.email())
            );
        });
        Customer customerToSave = CustomerDtoMapper.map(customerDto);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return CustomerDtoMapper.map(savedCustomer);
    }

    public CustomerDto findCustomerById(String id) {
        return customerRepository.findById(id)
                .map(CustomerDtoMapper::map)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id %s not found".formatted(id)));
    }

    public List<CustomerDto> findAllCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(--page, size))
                .stream()
                .map(CustomerDtoMapper::map)
                .toList();
    }
}
