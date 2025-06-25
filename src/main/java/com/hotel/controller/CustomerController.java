package com.hotel.controller;

import com.hotel.dto.CustomerDTO;
import com.hotel.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/name")
    public ResponseEntity<Map<String, Object>> searchByName(@RequestParam(required = false) String name) {
        try {
            List<CustomerDTO> customers;
            if (name != null && !name.trim().isEmpty()) {
                String searchName = name.trim().toLowerCase();
                String[] searchTerms = searchName.split("\\s+");
                String firstTerm = searchTerms[0];
                customers = customerService.searchCustomersByName(firstTerm);
            } else {
                customers = customerService.getAllCustomers(null, null, null);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("total", customers.size());
            response.put("customers", customers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@RequestParam(required = false) Long id, @RequestParam(required = false) String phone, @RequestParam(required = false) String idNumber) {
        List<CustomerDTO> customers = customerService.getAllCustomers(id, phone, idNumber);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/with-booking-history")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithBookingHistory() {
        List<CustomerDTO> customers = customerService.getCustomersWithBookingHistory();
        return ResponseEntity.ok(customers);
    }
} 