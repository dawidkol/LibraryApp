package pl.dk.libraryapp.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.libraryapp.customer.dtos.CustomerDto;
import pl.dk.libraryapp.exceptions.CustomerAlreadyExistsException;


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
}
