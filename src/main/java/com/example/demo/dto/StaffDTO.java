package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StaffDTO {
    private Long id;
    private String name;
    private String sdt;
    private String email;
    private BigDecimal salary;
    private String username;
}
