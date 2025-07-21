package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation_Room> reservation_rooms;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsedService> usedServices;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Future
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private BigDecimal totalAmount;
    private ReservationStatus reservationStatus;
    private String note;
}
