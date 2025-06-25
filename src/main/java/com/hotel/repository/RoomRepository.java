package com.hotel.repository;

import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.hotel.entity.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomType(RoomType roomType);
    List<Room> findByStatus(RoomStatus status);
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByRoomTypeAndStatus(RoomType roomType, RoomStatus status);
} 