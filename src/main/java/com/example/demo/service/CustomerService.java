package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> GetAllCustomer();
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO UpdateCustomer(CustomerDTO dto, Long id);
    void DeleteCustomer(Long id);
    List<CustomerDTO> getCustomerBy(Long id, String name, String phone);
    List<CustomerDTO> getName(String name);
}
