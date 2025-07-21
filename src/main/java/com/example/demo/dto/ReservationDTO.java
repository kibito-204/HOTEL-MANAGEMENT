package com.example.demo.dto;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.Room;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ReservationDTO {
    private Long id;
    private Long customerId;
    private String CustomerName;
    private List<RoomDTO> rooms;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private ReservationStatus reservationStatus;
    private BigDecimal totalAmount;
    private String note;
    private Set<String> services;
}
