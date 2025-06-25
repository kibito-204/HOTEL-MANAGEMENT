package com.hotel.controller;

import com.hotel.dto.ReservationDTO;
import com.hotel.entity.ReservationStatus;
import com.hotel.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/available-rooms")
    public ResponseEntity<List<com.hotel.entity.Room>> getAvailableRooms(
            @RequestParam com.hotel.entity.RoomType roomType,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime end) {
        List<com.hotel.entity.Room> availableRooms = reservationService.findAvailableRooms(roomType, start, end);
        return ResponseEntity.ok(availableRooms);
    }


    @PostMapping("/{id}/check-in")
    public ResponseEntity<String> checkIn(@PathVariable Long id) {
        reservationService.checkIn(id);
        return ResponseEntity.ok("Check-in thành công!");
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<String> checkOut(@PathVariable Long id) {
        String invoice = reservationService.checkOut(id);
        return ResponseEntity.ok(invoice);
    }
    @GetMapping  
    public ResponseEntity<List<ReservationDTO>> getReservation(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) Long roomId){
            List<ReservationDTO> reservations = reservationService.getReservation(id, customerId, roomId);
            return ResponseEntity.ok(reservations);
        }
} 