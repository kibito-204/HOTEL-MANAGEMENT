package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @NotBlank(message ="Số điện thoại không được để trống")
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;
    @NotBlank(message = "Email không được để trống")
    @Column(unique = true)
    private String email;
    private String address;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservation;
}
