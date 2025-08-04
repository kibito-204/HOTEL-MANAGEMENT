package com.example.demo.service.Impl;

import com.example.demo.dto.Reservation_roomDTO;
import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.Reservation_roomRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.Reservation_RoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Reservation_RoomServiceImpl implements Reservation_RoomService {
    @Autowired
    private Reservation_roomRepository reservation_roomRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;
//    @Override
//    public Reservation_roomDTO addRoom(Reservation_roomDTO dto){
//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new ResourceNotFoundException("Phòng không tìm thấy"));
//        BigDecimal price = room.getPrice();
//        Reservation reservation = reservationRepository.findById(dto.getReservationId())
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
//        if(room.getRoomStatus()!= RoomStatus.AVAILABLE){
//            throw new RuntimeException("Phòng không có sẵn");
//        }
//        List<Reservation_Room> BookedRoom = reservation_roomRepository.findByRoomId(dto.getRoomId());
//        for(Reservation_Room  x : BookedRoom){
//            if(overlapping(reservation.getCheckin(),reservation.getCheckout(),x.getReservation().getCheckin(),x.getReservation().getCheckout())){
//                throw new RuntimeException("Phòng đã được đặt trong khoảng thời gian này");
//            }
//        }
//        Reservation_Room r = new Reservation_Room();
//        r.setReservation(reservation);
//        r.setRoom(room);
//        BeanUtils.copyProperties(dto,r);
//        r.setPrice(price);
//        r = reservation_roomRepository.save(r);
//        BeanUtils.copyProperties(r, dto);
//        dto.setPrice(price);
//        return dto;
//    }
//    public boolean overlapping(LocalDateTime st1, LocalDateTime e1, LocalDateTime st2, LocalDateTime e2){
//        return (st1.isBefore(e2) && st2.isBefore(e1));
//    }
    @Override
    public void deleteRoom(Long reservationId, Long roomId){
        Reservation_Room r = reservation_roomRepository.findByRoomIdAndReservationId(roomId,reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
        reservation_roomRepository.delete(r);
    }
//    @Override
//    public Reservation_roomDTO updateRoom(Long reid, Long rid, Reservation_roomDTO dto){
//        Reservation reservation = reservationRepository.findById(reid)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
//        Reservation_Room r = reservation_roomRepository.findByRoomIdAndReservationId(rid, reid)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt phòng"));
//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));
//        if(room.getRoomStatus() != RoomStatus.AVAILABLE){
//            throw new RuntimeException("Phòng hiện không có sẵn");
//        }
//        List<Reservation_Room> BookedRoom = reservation_roomRepository.findByRoomId(dto.getRoomId());
//        for(Reservation_Room  x : BookedRoom){
//            if(x.getRoom().getId().equals(dto.getRoomId())){
//                continue;
//            }
//            if(overlapping(reservation.getCheckin(),reservation.getCheckout(),x.getReservation().getCheckin(),x.getReservation().getCheckout())){
//                throw new RuntimeException("Phòng đã được đặt trong khoảng thời gian này");
//            }
//        }
//        BeanUtils.copyProperties(dto, r,"id", "reservationId", "price");
//        BigDecimal price = room.getPrice();
//        r.setRoom(room);
//        r.setPrice(price);
//        r = reservation_roomRepository.save(r);
//        BeanUtils.copyProperties(r,dto);
//        dto.setPrice(price);
//        dto.setReservationId(reid);
//        return dto;
//    }
    @Override
    public List<RoomDTO> findAvailable(RoomType roomType, LocalDateTime checkin, LocalDateTime checkout){
        if(roomType == null){
            List<Room> availableRooms = roomRepository.findByRoomStatus(RoomStatus.AVAILABLE);
            List<Long> BookedRoom = reservation_roomRepository.findBookedRoomId(checkin, checkout);
            List<RoomDTO> abc = availableRooms.stream()
                    .filter(room -> !BookedRoom.contains(room.getId()))
                    .map(room -> {
                        RoomDTO dto = new RoomDTO();
                        BeanUtils.copyProperties(room, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
            return abc;
        }
        else {
            List<Room> availableRooms = roomRepository.findByRoomTypeAndRoomStatus(roomType, RoomStatus.AVAILABLE);
            List<Long> BookedRoom = reservation_roomRepository.findBookedRoomId(checkin, checkout);
            List<RoomDTO> abc = availableRooms.stream()
                    .filter(room -> !BookedRoom.contains(room.getId()))
                    .map(room -> {
                        RoomDTO dto = new RoomDTO();
                        BeanUtils.copyProperties(room, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
            return abc;
        }
    }
//    @Override
//    public List<Reservation_roomDTO> getAllReservationRoom(){
//        return reservation_roomRepository.findAll().stream()
//                .map(r -> {
//                    Reservation_roomDTO dto = new Reservation_roomDTO();
//                    BeanUtils.copyProperties(r, dto);
//                    dto.setReservationId(r.getReservation().getId());
//                    dto.setRoomId(r.getRoom().getId());
//                    return dto;
//                }).collect(Collectors.toList());
//    }
}
