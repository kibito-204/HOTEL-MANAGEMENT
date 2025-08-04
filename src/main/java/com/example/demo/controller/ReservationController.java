package com.example.demo.controller;


import com.example.demo.dto.ReservationDTO;
import com.example.demo.dto.ReservationDTO1;
import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @PostMapping("/booking")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<ReservationDTO> CreateReservation(@RequestBody ReservationDTO1 dto) {
        ReservationDTO r = reservationService.CreateReservation(dto);
        return ResponseEntity.ok(r);
    }
    @GetMapping()
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<List<ReservationDTO>> GetReservation(){
        List<ReservationDTO> dto = reservationService.GetReservation();
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<ReservationDTO> UpdateReservation(@RequestBody ReservationDTO1 dto,@PathVariable Long id){
        ReservationDTO reservationDTO = reservationService.UpdateReservation(dto, id);
        return ResponseEntity.ok(reservationDTO);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<Void> DeleteReservation(@PathVariable Long id){
            reservationService.DeleteReservation(id);
            return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<List<ReservationDTO>> SearchReservation(@RequestParam(required = false)ReservationStatus status){
        List<ReservationDTO> r = reservationService.SearchReservation(status);
        return ResponseEntity.ok(r);
    }
    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<String> checkIn(@PathVariable Long id){
        reservationService.CheckIn(id);
        return ResponseEntity.ok("Check-in thành công");
    }
    @PostMapping("/{id}/checkout")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<String> checkOut(@PathVariable Long id){
        String abc = reservationService.CheckOut(id);
        return ResponseEntity.ok(abc);
    }
}
