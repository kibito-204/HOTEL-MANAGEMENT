package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findById(Long id);
    @Query("select c from Customer c where lower(c.name) like lower(concat(:name,'%')) ")
    List<Customer> findByName(String name);

}
