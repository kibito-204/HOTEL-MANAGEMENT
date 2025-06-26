package com.hotel.service.impl;

import com.hotel.dto.RoomDTO;
import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.repository.RoomRepository;
import com.hotel.service.RoomService;
import com.hotel.entity.RoomType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = new Room();
        BeanUtils.copyProperties(roomDTO, room);
        if(roomDTO.getStatus() == null){
            room.setStatus(RoomStatus.AVAILABLE);
        }
        room = roomRepository.save(room);
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        BeanUtils.copyProperties(roomDTO, room, "id", "roomNumber");
        room = roomRepository.save(room);
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }



    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> {
                    RoomDTO roomDTO = new RoomDTO();
                    BeanUtils.copyProperties(room, roomDTO);
                    return roomDTO;
                })
                .collect(Collectors.toList());
    }

  
    @Override
    public RoomDTO updateRoomStatus(Long id, RoomStatus status){
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        room.setStatus(status);
        room = roomRepository.save(room);
        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }

    @Override
    public List<RoomDTO> findRooms(RoomStatus status, RoomType type, String number) {
        return roomRepository.findAll().stream()
            .filter(room -> status == null || room.getStatus() == status)
            .filter(room -> type ==null || room.getRoomType() == type)
            .filter(room -> number == null || room.getRoomNumber().equals(number))
            .map(room -> {
                RoomDTO roomDTO = new RoomDTO();
                BeanUtils.copyProperties(room, roomDTO);
                return roomDTO;
            })
            .collect(Collectors.toList());
    }
}
