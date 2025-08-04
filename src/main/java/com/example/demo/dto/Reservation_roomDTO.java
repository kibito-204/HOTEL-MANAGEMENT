package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class Reservation_roomDTO {
    private Long id;
    private Long reservationId;
    private Long roomId;
    private BigDecimal price;
}
