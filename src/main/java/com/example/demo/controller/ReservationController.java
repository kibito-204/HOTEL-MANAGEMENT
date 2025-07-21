package com.example.demo.controller;


import com.example.demo.dto.ReservationDTO;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> CreateReservation(@RequestBody ReservationDTO dto) {
        ReservationDTO reservation = reservationService.CreateReservation(dto);
        return ResponseEntity.ok(reservation);
    }
    @GetMapping("")
    public ResponseEntity<List<ReservationDTO>> GetReservation(){
        List<ReservationDTO> dto = reservationService.GetReservation();
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ReservationDTO> UpdateReservation(@RequestBody ReservationDTO dto,@PathVariable Long id){
        ReservationDTO reservationDTO = reservationService.UpdateReservation(dto, id);
        return ResponseEntity.ok(reservationDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> DeleteReservation(@PathVariable Long id){
            reservationService.DeleteReservation(id);
            return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<ReservationDTO>> SearchReservation(@RequestParam(required = false)String name,@RequestParam(required = false) String sdt, @RequestParam(required = false) Long id){
        List<ReservationDTO> r = reservationService.SearchReservation(name, sdt, id);
        return ResponseEntity.ok(r);
    }
    @PostMapping("/{id}/checkin")
    public ResponseEntity<String> checkIn(@PathVariable Long id){
        reservationService.CheckIn(id);
        return ResponseEntity.ok("Check-in thành công");
    }
    @PostMapping("/{id}/checkout")
    public ResponseEntity<String> checkOut(@PathVariable Long id){
        String abc = reservationService.CheckOut(id);
        return ResponseEntity.ok(abc);
    }
}
