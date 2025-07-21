package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UsedServiceDTO {
    private Long id;
    private Long reservationId;
    private Long serviceId;
    private BigDecimal price;
    private String note;
}
