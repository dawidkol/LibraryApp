package pl.dk.libraryapp.customer;

import pl.dk.libraryapp.customer.dtos.CustomerDto;

class CustomerDtoMapper {

    public static CustomerDto map(Customer customer) {
        return CustomerDto.builder()
                .id(customer.id())
                .firstName(customer.firstName())
                .lastName(customer.lastName())
                .email(customer.email())
                .build();
    }

    public static Customer map(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.id())
                .firstName(customerDto.firstName())
                .lastName(customerDto.lastName())
                .email(customerDto.email())
                .build();
    }
}
