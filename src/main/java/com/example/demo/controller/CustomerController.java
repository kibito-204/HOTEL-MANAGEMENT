package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/allCustomer")
    public ResponseEntity<List<CustomerDTO>> GetAllCustomer(){
        List<CustomerDTO> dto = customerService.GetAllCustomer();
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> CreateCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerDTO dto = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> UpdateCustomer(@RequestBody CustomerDTO dto, @PathVariable Long id){
        CustomerDTO customer = customerService.UpdateCustomer(dto, id);
        return ResponseEntity.ok(customer);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> DeleteCustomer(@RequestParam Long id){
        customerService.DeleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getBy")
    public ResponseEntity<List<CustomerDTO>> GetCustomerBy(@RequestParam(required = false)Long id,@RequestParam(required = false) String name,@RequestParam(required = false) String phone){
        if(name != null && !name.trim().isEmpty()){
            name = name.trim();
        }
        List<CustomerDTO> dto = customerService.getCustomerBy(id, name, phone);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/getName")
    public ResponseEntity<List<CustomerDTO>> getName(@RequestParam String name){
        if(name != null || !name.trim().isEmpty()){
            name = name.trim();
        }
        List<CustomerDTO> dto = customerService.getName(name);
        return ResponseEntity.ok(dto);
    }
}
