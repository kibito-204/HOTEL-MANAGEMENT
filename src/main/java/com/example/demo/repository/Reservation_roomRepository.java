package com.example.demo.repository;

import com.example.demo.entity.Reservation_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Reservation_roomRepository extends JpaRepository<Reservation_Room,Long> {
    List<Reservation_Room> findByRoomId(Long id);
    List<Reservation_Room> findByReservationId(Long id);
    Optional<Reservation_Room> findByRoomIdAndReservationId(Long roomId, Long reservationId);
    @Query("select rr.room.id from Reservation_Room rr where (rr.reservation.checkin < :end and rr.reservation.checkout > :start)")
    List<Long> findBookedRoomId(LocalDateTime start, LocalDateTime end);

}
