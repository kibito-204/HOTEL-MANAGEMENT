package com.example.demo.repository;

import com.example.demo.entity.Room;
import com.example.demo.entity.RoomStatus;
import com.example.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomTypeAndRoomStatus(RoomType roomType, RoomStatus status);
    @Query("select r.roomStatus, count(r) from Room r group by r.roomStatus")
    List<Object[]> listRoomStatus();
    List<Room> findByRoomStatus(RoomStatus roomStatus);
}
