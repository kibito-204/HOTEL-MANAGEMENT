package com.hotel.service;

import com.hotel.dto.ServiceDTO;
import java.util.List;

public interface ServiceService {
    ServiceDTO createService(ServiceDTO serviceDTO);
    ServiceDTO updateService(Long id, ServiceDTO serviceDTO);
    void deleteService(Long id);
    List<ServiceDTO> getAllServices();
} 