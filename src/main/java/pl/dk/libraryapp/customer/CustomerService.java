package pl.dk.libraryapp.customer;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import pl.dk.libraryapp.customer.dtos.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto saveCustomer(CustomerDto customerDto);

    CustomerDto findCustomerById(String id);

    List<CustomerDto> findAllCustomers(int page, int size);

    void updateCustomer(String id, JsonMergePatch jsonMergePatch);

    void deleteCustomerById(String id);
}
