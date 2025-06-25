package com.hotel.repository;

import com.hotel.entity.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelServiceRepository extends JpaRepository<HotelService, Long> {
    List<HotelService> findByCategory(String category);
    List<HotelService> findByIsActive(Boolean isActive);
    List<HotelService> findByNameContainingIgnoreCase(String name);
} 