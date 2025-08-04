package com.example.demo.service.Impl;

import com.example.demo.dto.HotelServiceDTO;
import com.example.demo.entity.HotelService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.service.ServiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    HotelServiceRepository serviceRepository;
    @Override
    public List<HotelServiceDTO> getService(){
        return serviceRepository.findAll().stream()
                .map(service -> {
                    HotelServiceDTO dto = new HotelServiceDTO();
                    BeanUtils.copyProperties(service, dto);
                    return dto;
                }).collect(Collectors.toList());
    }
    @Override
    public HotelServiceDTO createService(HotelServiceDTO dto){
        HotelService service = new HotelService();
        BeanUtils.copyProperties(dto, service);
        service = serviceRepository.save(service);
        BeanUtils.copyProperties(service,dto);
        return dto;
    }
    @Override
    public void deleteService(Long id){
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ"));
        serviceRepository.delete(service);
    }
}
