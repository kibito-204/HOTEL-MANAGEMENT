package com.example.demo.service;

import com.example.demo.dto.Reservation_roomDTO;
import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.Reservation_Room;
import com.example.demo.entity.RoomType;

import java.time.LocalDateTime;
import java.util.List;

public interface Reservation_RoomService {
//    Reservation_roomDTO addRoom(Reservation_roomDTO dto);
    void deleteRoom(Long reservationId, Long roomId);
//    Reservation_roomDTO updateRoom(Long reid, Long rid, Reservation_roomDTO dto);
    List<RoomDTO> findAvailable(RoomType roomType, LocalDateTime checkin, LocalDateTime checkout);
//    List<Reservation_roomDTO> getAllReservationRoom();
}
