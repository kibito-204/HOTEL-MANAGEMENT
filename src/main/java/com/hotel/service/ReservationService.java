package com.hotel.service;

import com.hotel.dto.ReservationDTO;
import java.util.List;

public interface ReservationService {
    ReservationDTO createReservation(ReservationDTO reservationDTO);
    ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO);
    void deleteReservation(Long id);
    List<ReservationDTO> getAllReservations();
    List<ReservationDTO> getReservation(Long id, Long customerId, Long roomId);
    java.util.List<com.hotel.entity.Room> findAvailableRooms(com.hotel.entity.RoomType roomType, java.time.LocalDateTime start, java.time.LocalDateTime end);
    void checkIn(Long reservationId);
    String checkOut(Long reservationId);
} 