package com.hotel.dto;

import com.hotel.entity.RoomStatus;
import com.hotel.entity.RoomType;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomDTO {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal price;
    private String description;
    private Integer capacity;
    private RoomStatus status;
} 