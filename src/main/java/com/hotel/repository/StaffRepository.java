package com.hotel.repository;

import com.hotel.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
} 