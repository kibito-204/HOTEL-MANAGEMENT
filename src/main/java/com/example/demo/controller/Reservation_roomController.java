package com.example.demo.controller;

import com.example.demo.dto.Reservation_roomDTO;
import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.Reservation_Room;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomType;
import com.example.demo.service.Reservation_RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation_room")
public class Reservation_roomController {
    @Autowired
    private Reservation_RoomService reservation_roomService;
//    @PostMapping("")
//    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
//    public ResponseEntity<Reservation_roomDTO> addRoom(@RequestBody Reservation_roomDTO dto){
//        Reservation_roomDTO r = reservation_roomService.addRoom(dto);
//        return ResponseEntity.ok(r);
//    }
    @DeleteMapping("/{reservationId}/{roomId}")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long reservationId, @PathVariable Long roomId){
        reservation_roomService.deleteRoom(reservationId, roomId);
        return ResponseEntity.noContent().build();
    }
//    @PutMapping("/{reservationId}/{roomId}")
//    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
//    public ResponseEntity<Reservation_roomDTO> updateRoom(@PathVariable Long reservationId,@PathVariable Long roomId, @RequestBody Reservation_roomDTO dto){
//        Reservation_roomDTO dto1 = reservation_roomService.updateRoom(reservationId, roomId, dto);
//        return ResponseEntity.ok(dto1);
//    }
    @GetMapping("/available-room")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<List<RoomDTO>> findAvailable(@RequestParam(required = false) RoomType roomType, @RequestParam LocalDateTime checkin, @RequestParam LocalDateTime checkout){
        List<RoomDTO> rooms = reservation_roomService.findAvailable(roomType,checkin,checkout);
        return  ResponseEntity.ok(rooms);
    }
//    @GetMapping("/get")
//    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
//    public ResponseEntity<List<Reservation_roomDTO>> getReservation_Room(){
//        List<Reservation_roomDTO> rr = reservation_roomService.getAllReservationRoom();
//        return ResponseEntity.ok(rr);
//    }
}
