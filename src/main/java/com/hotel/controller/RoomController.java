package com.hotel.controller;

import com.hotel.dto.RoomDTO;
import com.hotel.entity.RoomStatus;
import com.hotel.entity.RoomType;
import com.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Thêm phòng mới
    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    // Sửa thông tin phòng
    @PutMapping("/{id}")
    public RoomDTO updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDTO) {
        return roomService.updateRoom(id, roomDTO);
    }

    // Xóa phòng
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Thay đổi trạng thái phòng
    @PutMapping("/{id}/status")
    public RoomDTO updateRoomStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
        return roomService.updateRoomStatus(id, status);
    }

    public ResponseEntity<List<RoomDTO>> findRooms(
        @RequestParam(required = false) RoomStatus status,
        @RequestParam(required = false) RoomType type,
        @RequestParam(required = false) String number){
            List<RoomDTO> rooms = roomService.findRooms(status, type, number);
            return ResponseEntity.ok(rooms);
        }
} 