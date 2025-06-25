package com.hotel.repository;

import com.hotel.entity.ServiceUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceUsageRepository extends JpaRepository<ServiceUsage, Long> {
    List<ServiceUsage> findByReservationId(Long reservationId);

    @Query("SELECT COALESCE(SUM(su.unitPrice), 0) FROM ServiceUsage su WHERE su.reservation.id = :reservationId")
    BigDecimal sumTotalPriceByReservationId(Long reservationId);

    @Query("SELECT su.service.name FROM ServiceUsage su WHERE su.reservation.checkOut BETWEEN :from AND :to GROUP BY su.service.name ")
    java.util.List<Object[]> findTopUsedServices(java.time.LocalDateTime from, java.time.LocalDateTime to, org.springframework.data.domain.Pageable pageable);

    default java.util.List<Object[]> findTopUsedServices(java.time.LocalDateTime from, java.time.LocalDateTime to, int topN) {
        return findTopUsedServices(from, to, org.springframework.data.domain.PageRequest.of(0, topN));
    }
} 