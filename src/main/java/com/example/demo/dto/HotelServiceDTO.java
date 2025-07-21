package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelServiceDTO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
}
