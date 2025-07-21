package com.example.demo.service;

import com.example.demo.dto.ReservationDTO;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationDTO CreateReservation(ReservationDTO dto);
    List<ReservationDTO> GetReservation();
    ReservationDTO UpdateReservation(ReservationDTO dto, Long id);
    void DeleteReservation(Long id);
    List<ReservationDTO> SearchReservation(String name, String sdt, Long id);
    void CheckIn(Long id);
    String CheckOut(Long id);
}
