package com.example.demo.service;

import com.example.demo.dto.UsedServiceDTO;

public interface UsedServiceService {
    UsedServiceDTO addService(UsedServiceDTO dto);
    String sumTotal(Long id);
}
