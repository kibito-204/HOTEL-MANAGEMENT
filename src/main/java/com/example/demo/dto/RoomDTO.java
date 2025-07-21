package com.example.demo.dto;

import com.example.demo.entity.RoomStatus;
import com.example.demo.entity.RoomType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDTO {
    private Long id;
    private String number;
    private RoomStatus roomStatus;
    private RoomType roomType;
    private BigDecimal price;
    private String note;
}
