package com.hotel.dto;

import com.hotel.entity.ReservationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.hotel.entity.RoomType;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Getter
@Setter
public class ReservationDTO {
    private Long id;

    private Long customerId;

    @NotNull(message = "ID phòng không được để trống")
    private Long roomId;

    @NotNull(message = "Thời gian check-in không được để trống")
    @Future(message = "Thời gian check-in phải là thời gian trong tương lai")
    private LocalDateTime checkIn;

    @NotNull(message = "Thời gian check-out không được để trống")
    @Future(message = "Thời gian check-out phải là thời gian trong tương lai")
    private LocalDateTime checkOut;

    private ReservationStatus status;

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tổng tiền phải lớn hơn 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Số lượng khách không được để trống")
    @Min(value = 1, message = "Số lượng khách phải lớn hơn 0")
    @Max(value = 10, message = "Số lượng khách không được vượt quá 10 người")
    private Integer numberOfGuests;

    @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
    private String note;

    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    // Thông tin bổ sung để hiển thị
    private String customerName;
    private String roomNumber;
    private RoomType roomType;
    // Danh sách tên dịch vụ đã sử dụng
    private java.util.List<String> services;
} 