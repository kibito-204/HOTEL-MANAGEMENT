package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}$")
    private String sdt;
    @Column(unique = true)
    private String email;
    private BigDecimal salary;
    private Role role;
}
