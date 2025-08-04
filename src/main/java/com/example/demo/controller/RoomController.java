package com.example.demo.controller;

import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.RoomStatus;
import com.example.demo.entity.RoomType;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @GetMapping("")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<List<RoomDTO>> GetRoom(@RequestParam(required = false) String number, @RequestParam(required = false)RoomType roomType,@RequestParam(required = false) RoomStatus roomStatus){
        List<RoomDTO> dto = roomService.GetRoom(number, roomType,roomStatus);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomDTO> CreateRoom(@RequestBody RoomDTO dto){
        RoomDTO room = roomService.CreateRoom(dto);
        return ResponseEntity.ok(room);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomDTO> UpdateRoom(@RequestBody RoomDTO dto,@PathVariable Long id){
        RoomDTO room = roomService.UpdateRoom(dto,id);
        return ResponseEntity.ok(room);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> DeleteRoom(@PathVariable Long id){
        roomService.DeleteRoom(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateStatus/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<RoomDTO> UpdateStatusRoom(@RequestBody RoomDTO dto,@PathVariable Long id){
        RoomDTO room = roomService.UpdateStatusRoom(dto.getRoomStatus(), id);
        return ResponseEntity.ok(room);
    }
}
