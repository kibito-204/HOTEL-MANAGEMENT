package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Reservation_roomDTO {
    private Long id;
    private Long reservationId;
    private Long roomId;
    private BigDecimal price;
}
