package com.hotel.service;

import com.hotel.dto.RoomDTO;
import com.hotel.entity.RoomStatus;
import com.hotel.entity.RoomType;
import java.util.List;

public interface RoomService {
    RoomDTO createRoom(RoomDTO roomDTO);
    RoomDTO updateRoom(Long id, RoomDTO roomDTO);
    void deleteRoom(Long id);
    RoomDTO getRoomById(Long id);
    List<RoomDTO> getAllRooms();
    RoomDTO updateRoomStatus(Long id, RoomStatus status);
    List<RoomDTO> findRooms(RoomStatus status, RoomType type, String number);
}
