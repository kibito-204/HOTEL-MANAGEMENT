package com.hotel.service.impl;

import com.hotel.dto.ServiceDTO;
import com.hotel.entity.HotelService;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.ServiceRepository;
import com.hotel.service.ServiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        HotelService service = new HotelService();
        BeanUtils.copyProperties(serviceDTO, service);
        service = serviceRepository.save(service);
        BeanUtils.copyProperties(service, serviceDTO);
        return serviceDTO;
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ"));
        BeanUtils.copyProperties(serviceDTO, service, "id");
        service = serviceRepository.save(service);
        BeanUtils.copyProperties(service, serviceDTO);
        return serviceDTO;
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public List<ServiceDTO> getAllServices(){
        return serviceRepository.findAll().stream()
        .map(service -> {
            ServiceDTO dto = new ServiceDTO();
            BeanUtils.copyProperties(service,dto);
            return dto;
        })  
        .collect(Collectors.toList());
    }
} 