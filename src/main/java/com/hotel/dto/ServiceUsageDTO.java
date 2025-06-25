package com.hotel.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class ServiceUsageDTO {
    private Long id;
    private Long reservationId;
    private Long serviceId;
    private String notes;
    private String serviceName;
    private BigDecimal unitPrice;
} 