package com.example.demo.service;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.dto.ReservationDTO1;
import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationDTO CreateReservation(ReservationDTO1 dto);
    List<ReservationDTO> GetReservation();
    ReservationDTO UpdateReservation(ReservationDTO1 dto1, Long id);
    void DeleteReservation(Long id);
    List<ReservationDTO> SearchReservation(ReservationStatus status);
    void CheckIn(Long id);
    String CheckOut(Long id);
}
