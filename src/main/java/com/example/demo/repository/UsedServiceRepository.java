package com.example.demo.repository;

import com.example.demo.entity.UsedService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedServiceRepository extends JpaRepository<UsedService, Long> {
}
