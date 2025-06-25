package com.hotel.dto;

import com.hotel.entity.StaffRole;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StaffDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private BigDecimal salary;
    private com.hotel.entity.StaffRole role;
} 