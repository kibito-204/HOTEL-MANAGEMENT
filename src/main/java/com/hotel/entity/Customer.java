package com.hotel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 200)
    private String address;

    @Column(nullable = false, unique = true, length = 20)
    private String idNumber;

    @Column(nullable = false, length = 20)
    private String idType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private java.util.List<Reservation> reservations;
} 