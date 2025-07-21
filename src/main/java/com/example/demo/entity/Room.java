package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Loại phòng không được để trống")
    private RoomType roomType;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Trạng thái phòng không được để trống")
    private RoomStatus roomStatus;
    @NotNull
    private String number;
    @NotNull
    private BigDecimal price;
    private String note;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation_Room> reservation_rooms;
}
