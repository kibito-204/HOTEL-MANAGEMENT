package com.hotel.repository;

import com.hotel.entity.Reservation;
import com.hotel.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerId(Long customerId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCheckInBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByCheckOutBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);
    List<Reservation> findByCustomerIdAndStatus(Long customerId, ReservationStatus status);
    @Query("SELECT r.room.id FROM Reservation r WHERE (:start < r.checkOut AND :end > r.checkIn)")
    java.util.List<Long> findBookedRoomIds(java.time.LocalDateTime start, java.time.LocalDateTime end);
} 