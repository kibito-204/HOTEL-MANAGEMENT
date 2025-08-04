package com.example.demo.service.Impl;

import com.example.demo.dto.UsedServiceDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.Reservation_roomRepository;
import com.example.demo.repository.UsedServiceRepository;
import com.example.demo.service.UsedServiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsedServiceServiceImpl implements UsedServiceService {
    @Autowired
    private UsedServiceRepository  usedServiceRepository;
    @Autowired
    private HotelServiceRepository hotelServiceRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private Reservation_roomRepository reservation_roomRepository;
    @Override
    public UsedServiceDTO addService(UsedServiceDTO dto){
        HotelService service = hotelServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ"));
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
        if((reservation.getReservationStatus() != ReservationStatus.CONFIRMED)&&(reservation.getReservationStatus() != ReservationStatus.PENDING)){
            throw new RuntimeException("Không thể đặt dịch vụ");
        }
        UsedService usedService =  new UsedService();
        BeanUtils.copyProperties(dto, usedService, "price");
        usedService.setService(service);
        usedService.setReservation(reservation);
        usedService.setPrice(service.getPrice());
        usedService = usedServiceRepository.save(usedService);
        BeanUtils.copyProperties(usedService, dto);
        return dto;
    }
    @Override
    public String sumTotal(Long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
        List<Reservation_Room> a = reservation_roomRepository.findByReservationId(reservation.getId());
        BigDecimal total = BigDecimal.ZERO;
        for(Reservation_Room x:a){
            total = total.add(x.getPrice());
        }
        BigDecimal priceService = BigDecimal.ZERO;
        for(UsedService x : reservation.getUsedServices()){
            priceService = priceService.add(x.getPrice());
        }
        total = total.add(priceService);
        return "Tổng chi phí dịch vụ: " + total;
    }
}
