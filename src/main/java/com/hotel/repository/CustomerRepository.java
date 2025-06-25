package com.hotel.repository;

import com.hotel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByIdNumber(String idNumber);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT(:name, '%'))")
    List<Customer> findByNameStartingWithIgnoreCase(String name);
    
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.reservations r")
    List<Customer> findCustomersWithBookings();
} 