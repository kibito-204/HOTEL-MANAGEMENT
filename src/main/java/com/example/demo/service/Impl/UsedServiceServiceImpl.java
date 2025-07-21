package com.example.demo.service.Impl;

import com.example.demo.dto.UsedServiceDTO;
import com.example.demo.entity.HotelService;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.UsedService;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UsedServiceRepository;
import com.example.demo.service.UsedServiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UsedServiceServiceImpl implements UsedServiceService {
    @Autowired
    private UsedServiceRepository  usedServiceRepository;
    @Autowired
    private HotelServiceRepository hotelServiceRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public UsedServiceDTO addService(UsedServiceDTO dto){
        HotelService service = hotelServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));
        if((reservation.getReservationStatus() != ReservationStatus.CONFIRMED)&&(reservation.getReservationStatus() != ReservationStatus.PENDING)){
            throw new RuntimeException("Không thể đặt dịch vụ");
        }
        UsedService usedService =  new UsedService();
        BeanUtils.copyProperties(dto,usedService, "price");
        usedService.setService(service);
        usedService.setReservation(reservation);
        usedService.setPrice(service.getPrice());
        usedService = usedServiceRepository.save(usedService);
        BeanUtils.copyProperties(usedService, dto);
        return dto;
    }
    @Override
    public String sumTotal(Long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));
        BigDecimal sum = BigDecimal.ZERO;
        for(UsedService x: reservation.getUsedServices()){
            sum = sum.add(x.getPrice());
        }
        return "Tổng chi phí dịch vụ: " + sum;
    }
}
