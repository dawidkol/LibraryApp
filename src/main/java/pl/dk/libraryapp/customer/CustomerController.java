package pl.dk.libraryapp.customer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import java.net.URI;
import java.util.List;

import static pl.dk.libraryapp.constants.PaginationConstants.*;
import static pl.dk.libraryapp.constants.PaginationConstants.PAGE_SIZE_DEFAULT;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> registerCustomers(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomerDto = customerService.saveCustomer(customerDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomerDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedCustomerDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        CustomerDto customerById = customerService.findCustomerById(id);
        return ResponseEntity.ok(customerById);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(
            @RequestParam(required = false, defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) int size) {
        List<CustomerDto> allCustomers = customerService.findAllCustomers(page, size);
        return ResponseEntity.ok(allCustomers);
    }
}
