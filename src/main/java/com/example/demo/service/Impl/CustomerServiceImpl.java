package com.example.demo.service.Impl;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<CustomerDTO> GetAllCustomer(){
        return customerRepository.findAll().stream()
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    BeanUtils.copyProperties(customer, dto);
                    return dto;
                }).collect(Collectors.toList());
    }
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        List<Customer> customer1 = customerRepository.findAll();
        for(Customer x:customer1) {
            if (customerDTO.getPhone().equals(x.getPhone())){
                throw new InvalidRequestException("Số điện thoại đã bị trùng");
            }
            if(customerDTO.getEmail().equals(x.getEmail())){
                throw new InvalidRequestException("Email đã bị trùng");
            }
        }
        BeanUtils.copyProperties(customerDTO,customer);
        customer = customerRepository.save(customer);
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    @Override
    public CustomerDTO UpdateCustomer(CustomerDTO dto, Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));
        List<Customer> customer1 = customerRepository.findAll();
        for(Customer x : customer1) {
            if(x.getId().equals(customer.getId())){
                continue;
            }
            if (dto.getPhone().equals(x.getPhone())){
                throw new InvalidRequestException("Số điện thoại đã bị trùng");
            }
            if(dto.getEmail().equals(x.getEmail())){
                throw new InvalidRequestException(  "Email đã bị trùng");
            }
        }
        BeanUtils.copyProperties(dto, customer, "id");
        customer = customerRepository.save(customer);
        BeanUtils.copyProperties(customer, dto);
        return dto;
    }
    @Override
    public void DeleteCustomer(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));
        customerRepository.delete(customer);
    }
    @Override
    public List<CustomerDTO> getCustomerBy(Long id,String name,String phone){
        return customerRepository.findAll().stream()
                .filter(customer -> id == null || customer.getId().equals(id))
                .filter(customer -> name == null || customer.getName().equals(name))
                .filter(customer -> phone == null || customer.getPhone().equals(phone))
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    BeanUtils.copyProperties(customer,dto);
                    return dto;}
                ).collect(Collectors.toList());
    }
    @Override
    public List<CustomerDTO> getName(String name){
        List<Customer> customer = customerRepository.findByName(name);
        List<CustomerDTO> dtos = new ArrayList<>();
        for (Customer abc: customer){
            CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(abc,dto);
        dtos.add(dto);
        }
        return dtos;
    }
}
