package com.hotel.service;

import com.hotel.dto.ServiceUsageDTO;
import java.math.BigDecimal;

public interface ServiceUsageService {
    ServiceUsageDTO addServiceUsage(ServiceUsageDTO serviceUsageDTO);
    BigDecimal calculateTotalServiceCost(Long reservationId);
} 