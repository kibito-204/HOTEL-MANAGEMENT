package com.example.demo.service.Impl;

import com.example.demo.dto.RoomDTO;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomStatus;
import com.example.demo.entity.RoomType;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.RoomService;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public List<RoomDTO> GetRoom(String number, RoomType roomType, RoomStatus roomStatus){
        return roomRepository.findAll().stream()
                .filter(room -> number == null || room.getNumber().equals(number))
                .filter(room -> roomType == null || room.getRoomType().equals(roomType))
                .filter(room -> roomStatus == null || room.getRoomStatus().equals(roomStatus))
                .map(room -> {
                    RoomDTO dto = new RoomDTO();
                    BeanUtils.copyProperties(room, dto);
                    return dto;
                }).collect(Collectors.toList());
    }
    @Override
    public RoomDTO CreateRoom(RoomDTO dto){
        Room room = new Room();
        List<Room> abc = roomRepository.findAll();
        for(Room x : abc) {
            if (dto.getNumber().equals(x.getNumber())){
                throw new InvalidRequestException("Số phòng đã được tạo");
            }
        }
        BeanUtils.copyProperties(dto, room);
        room.setRoomStatus(RoomStatus.AVAILABLE);
        room = roomRepository.save(room);
        BeanUtils.copyProperties(room, dto);
        return dto;
    }
    @Override
    public RoomDTO UpdateRoom(RoomDTO dto, Long id){
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));
        List<Room> x = roomRepository.findAll();
        for(Room a:x){
            if(a.getId().equals(id)){
                continue;
            }
            if(dto.getNumber().equals(a.getNumber())){
                throw new ValidationException("Trùng phòng");
            }
        }
        BeanUtils.copyProperties(dto, room, "id","roomStatus");
        room = roomRepository.save(room);
        BeanUtils.copyProperties(room, dto);
        return dto;
    }
    @Override
    public void DeleteRoom(Long id){
        Room room = roomRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy phòng"));
        roomRepository.delete(room);
    }
    @Override
    public RoomDTO UpdateStatusRoom(RoomStatus status, Long id){
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));
        RoomDTO dto = new RoomDTO();
        room.setRoomStatus(status);
        room = roomRepository.save(room);
        BeanUtils.copyProperties(room, dto);
        return dto;
    }
}
