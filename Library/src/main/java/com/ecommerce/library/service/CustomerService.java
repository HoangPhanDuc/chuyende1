package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer update(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);
}
