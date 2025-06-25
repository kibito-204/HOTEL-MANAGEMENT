package com.hotel.service.impl;

import com.hotel.dto.ServiceUsageDTO;
import com.hotel.entity.HotelService;
import com.hotel.entity.Reservation;
import com.hotel.entity.ServiceUsage;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelServiceRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.ServiceUsageRepository;
import com.hotel.service.ServiceUsageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ServiceUsageServiceImpl implements ServiceUsageService {
    @Autowired
    private ServiceUsageRepository serviceUsageRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private HotelServiceRepository hotelServiceRepository;

    @Override
    public ServiceUsageDTO addServiceUsage(ServiceUsageDTO dto) {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đặt phòng"));
        if (reservation.getStatus() != com.hotel.entity.ReservationStatus.CONFIRMED) {
            throw new com.hotel.exception.ValidationException("Chỉ có thể thêm dịch vụ khi đặt phòng ở trạng thái CONFIRMED!");
        }
        HotelService service = hotelServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ"));
        ServiceUsage usage = new ServiceUsage();
        BeanUtils.copyProperties(dto, usage);
        usage.setReservation(reservation);
        usage.setService(service);
        usage.setUnitPrice(service.getPrice());
        usage = serviceUsageRepository.save(usage);
        BeanUtils.copyProperties(usage, dto);
        dto.setServiceName(service.getName());
        dto.setUnitPrice(service.getPrice());
        return dto;
    }



    @Override
    public BigDecimal calculateTotalServiceCost(Long reservationId) {
        return serviceUsageRepository.sumTotalPriceByReservationId(reservationId);
    }
} 