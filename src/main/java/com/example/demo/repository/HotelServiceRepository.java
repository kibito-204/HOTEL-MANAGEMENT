package com.example.demo.repository;

import com.example.demo.entity.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelServiceRepository extends JpaRepository<HotelService, Long> {
}
