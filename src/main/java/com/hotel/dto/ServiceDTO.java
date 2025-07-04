package com.hotel.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Boolean isActive;
} 