package com.example.demo.service;

import com.example.demo.dto.HotelServiceDTO;
import com.example.demo.entity.HotelService;

import java.util.List;

public interface ServiceService {
    List<HotelServiceDTO> getService();
    HotelServiceDTO createService(HotelServiceDTO dto);
    void deleteService(Long id);
}
