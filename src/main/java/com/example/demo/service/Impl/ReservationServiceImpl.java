package com.example.demo.service.Impl;


import com.example.demo.dto.ReservationDTO;
import com.example.demo.dto.Reservation_roomDTO;
import com.example.demo.dto.RoomDTO;
import com.example.demo.dto.UsedServiceDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.Reservation_roomRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.ReservationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private Reservation_roomRepository reservation_roomRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public ReservationDTO CreateReservation(ReservationDTO dto){
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(dto, reservation, "reservationStatus", "totalAmount", "customer");
        reservation.setCustomer(customer);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setTotalAmount(BigDecimal.ZERO);
        reservation = reservationRepository.save(reservation);
        BeanUtils.copyProperties(reservation, dto);
        dto.setCustomerName(customer.getName());
        return dto;
    }
    @Override
    public List<ReservationDTO> GetReservation(){
        return reservationRepository.findAll().stream()
                .map(r -> {
                    ReservationDTO dto = new ReservationDTO();
                    BeanUtils.copyProperties(r,dto);
                    dto.setCustomerId(r.getCustomer().getId());
                    dto.setCustomerName(r.getCustomer().getName());
                    List<Reservation_Room> a = reservation_roomRepository.findByReservationId(r.getId());
                    BigDecimal total = BigDecimal.ZERO;
                    for(Reservation_Room x:a){
                        total = total.add(x.getPrice());
                    }
                    BigDecimal totalService = BigDecimal.ZERO;
                    for(UsedService x : r.getUsedServices()){
                        totalService = totalService.add(x.getPrice());
                    }
                    total = total.add(totalService);
                    dto.setTotalAmount(total);
                    List<RoomDTO> abc = r.getReservation_rooms().stream()
                            .map(re ->{
                                Room room = re.getRoom();
                                RoomDTO roomDTO = new RoomDTO();
                                BeanUtils.copyProperties(room,roomDTO);
                                return roomDTO;
                            }).collect(Collectors.toList());
                    dto.setRooms(abc);
                    Set<String> usedService = r.getUsedServices()
                            .stream().map(ser ->{
                                String name = ser.getService().getName();
                                return name;
                            })
                            .collect(Collectors.toSet());
                    dto.setServices(usedService);
                    return dto;
                }).collect(Collectors.toList());
    }
    @Override
    public ReservationDTO UpdateReservation(ReservationDTO dto, Long id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));
        BeanUtils.copyProperties(dto,reservation,"id", "totalAmount");
        reservation = reservationRepository.save(reservation);
        BeanUtils.copyProperties(reservation, dto);
        dto.setCustomerId(reservation.getCustomer().getId());
        dto.setCustomerName(reservation.getCustomer().getName());
        List<Reservation_Room> a = reservation_roomRepository.findByReservationId(reservation.getId());
        BigDecimal total = BigDecimal.ZERO;
        for(Reservation_Room x:a){
            total = total.add(x.getPrice());
        }
        List<Long> roomIds = reservation_roomRepository.findByReservationId(id)
                .stream().map(re -> re.getRoom().getId())
                .collect(Collectors.toList());
        List<Reservation> reservations = reservationRepository.findAll();
        for(Reservation r : reservations){
            if(r.getId().equals(id)){
                continue;
            }
            if(overlapping(dto.getCheckin(),dto.getCheckout(),r.getCheckin(),r.getCheckout())){
                for(Reservation_Room abc : r.getReservation_rooms()){
                    if(roomIds.contains(abc.getRoom().getId())){
                        throw new RuntimeException("Trong khoảng thời gian này đã có phòng được đặt");
                    }
                }
            }
        }
        dto.setTotalAmount(total);
        List<RoomDTO> abc = reservation.getReservation_rooms().stream()
                .map(re ->{
                    Room room = re.getRoom();
                    RoomDTO roomDTO = new RoomDTO();
                    BeanUtils.copyProperties(room,roomDTO);
                    return roomDTO;
                }).collect(Collectors.toList());
        dto.setRooms(abc);
        return dto;
    }
    public boolean overlapping(LocalDateTime st1, LocalDateTime e1, LocalDateTime st2, LocalDateTime e2){
        return (st1.isBefore(e2) && st2.isBefore(e1));
    }
    @Override
    public void DeleteReservation(Long id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));
        reservationRepository.delete(reservation);
    }
    @Override
    public List<ReservationDTO> SearchReservation(String name, String sdt, Long id){
        return reservationRepository.findAll().stream()
                .filter(r -> name == null || r.getCustomer().getName().equals(name))
                .filter(r -> sdt == null || r.getCustomer().getPhone().equals(sdt))
                .filter(r -> id == null || r.getId().equals(id))
                .map(r -> {
                    ReservationDTO dto = new ReservationDTO();
                    BeanUtils.copyProperties(r, dto);
                    dto.setCustomerId(r.getCustomer().getId());
                    dto.setCustomerName(r.getCustomer().getName());
                    BigDecimal price = BigDecimal.ZERO;
                    for (Reservation_Room x : r.getReservation_rooms()) {
                        price = price.add(x.getPrice());
                    }
                    dto.setTotalAmount(price);
                    List<RoomDTO> abc = r.getReservation_rooms().stream()
                            .map(re ->{
                                Room room = re.getRoom();
                                RoomDTO dto1 =  new RoomDTO();
                                BeanUtils.copyProperties(room, dto1);
                                return dto1;
                            }).collect(Collectors.toList());
                    dto.setRooms(abc);
                    Set<String> usedService = r.getUsedServices()
                            .stream().map(ser ->{
                                String name1 = ser.getService().getName();
                                return name1;
                            })
                            .collect(Collectors.toSet());
                    dto.setServices(usedService);
                    return dto;
                }).collect(Collectors.toList());
    }
    @Override
    public void CheckIn(Long id){
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch đặt"));
        if(r.getReservationStatus() == ReservationStatus.CHECKOUTED){
            throw new RuntimeException("Phòng đã được check-out");
        }
        if(r.getReservationStatus()!=ReservationStatus.CONFIRMED){
            throw new RuntimeException("Phòng chưa được xác nhận");
        }
        r.setReservationStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.save(r);
    }
    @Override
    public String CheckOut(Long id){
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lịch đặt không tìm thấy"));
        if(r.getReservationStatus() == ReservationStatus.CHECKOUTED){
            throw new RuntimeException("Phòng đã checkout");
        }
        if(r.getReservationStatus() != ReservationStatus.CHECKED_IN){
            throw new RuntimeException("Phòng chưa check-in");
        }
        r.setReservationStatus(ReservationStatus.CHECKOUTED);
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalService = BigDecimal.ZERO;
        for(UsedService x : r.getUsedServices()){
            totalService = totalService.add(x.getPrice());
        }
        long nights = ChronoUnit.DAYS.between(r.getCheckin(),r.getCheckout());
        if(nights == 0){
            nights = 1;
        }
        for(Reservation_Room abc : r.getReservation_rooms()){
            total = total.add(BigDecimal.valueOf(nights).multiply(abc.getPrice()));
        }
        total = total.add(totalService);
        r.setTotalAmount(total);
        reservationRepository.save(r);
        return "Tổng chi phí: " + total;
    }
}
