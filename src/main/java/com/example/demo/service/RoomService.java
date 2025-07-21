package com.example.demo.service;

import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.RoomStatus;
import com.example.demo.entity.RoomType;

import java.util.List;

public interface RoomService {
    List<RoomDTO> GetRoom(String number, RoomType roomType, RoomStatus roomStatus);
    RoomDTO CreateRoom(RoomDTO dto);
    RoomDTO UpdateRoom(RoomDTO dto, Long id);
    void DeleteRoom(Long id);
    RoomDTO UpdateStatusRoom(RoomStatus status,Long id);
}
