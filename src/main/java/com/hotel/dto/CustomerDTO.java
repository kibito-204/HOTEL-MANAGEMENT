package com.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải từ 2 đến 100 ký tự")
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    private String address;

    @NotBlank(message = "Số CMND/CCCD không được để trống")
    @Pattern(regexp = "^[0-9]{9}|[0-9]{12}$", message = "Số CMND/CCCD không hợp lệ")
    private String idNumber;

    @NotBlank(message = "Loại giấy tờ không được để trống")
    @Pattern(regexp = "^(CMND|CCCD|Passport)$", message = "Loại giấy tờ phải là CMND, CCCD hoặc Passport")
    private String idType;

    private boolean hasBookingHistory;
} 